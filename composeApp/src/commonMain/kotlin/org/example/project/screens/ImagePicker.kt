package org.example.project.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

enum class PermissionDeniedType {
    CAMERA_RATIONALE,
    CAMERA_SETTINGS,
    GALLERY_SETTINGS,
}

data class ImagePickerLaunchers(
    val launchGallery: () -> Unit,
    val launchCamera: () -> Unit,
    val permissionDenied: MutableState<PermissionDeniedType?> = mutableStateOf(null),
    val retryPermission: () -> Unit = {},
)

@Composable
expect fun rememberImagePickerLaunchers(onResult: (ByteArray?) -> Unit): ImagePickerLaunchers

expect fun openAppSettings()

@Composable
fun PermissionDeniedDialog(launchers: ImagePickerLaunchers) {
    val deniedType = launchers.permissionDenied.value ?: return

    val isGallery = deniedType == PermissionDeniedType.GALLERY_SETTINGS

    AlertDialog(
        onDismissRequest = { launchers.permissionDenied.value = null },
        title = { Text(if (isGallery) "Photo Library Access Required" else "Camera Access Required") },
        text = {
            Text(
                when (deniedType) {
                    PermissionDeniedType.CAMERA_RATIONALE ->
                        "Camera access is needed to take a profile photo. Please allow camera access."
                    PermissionDeniedType.CAMERA_SETTINGS ->
                        "Camera access was denied. You can enable it in the app settings."
                    PermissionDeniedType.GALLERY_SETTINGS ->
                        "Photo library access was denied. You can enable it in the app settings."
                },
            )
        },
        confirmButton = {
            TextButton(onClick = {
                launchers.permissionDenied.value = null
                when (deniedType) {
                    PermissionDeniedType.CAMERA_RATIONALE -> launchers.retryPermission()
                    PermissionDeniedType.CAMERA_SETTINGS -> openAppSettings()
                    PermissionDeniedType.GALLERY_SETTINGS -> openAppSettings()
                }
            }) {
                Text(
                    when (deniedType) {
                        PermissionDeniedType.CAMERA_RATIONALE -> "Allow"
                        PermissionDeniedType.CAMERA_SETTINGS -> "Open Settings"
                        PermissionDeniedType.GALLERY_SETTINGS -> "Open Settings"
                    },
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { launchers.permissionDenied.value = null }) {
                Text("Cancel")
            }
        },
    )
}
