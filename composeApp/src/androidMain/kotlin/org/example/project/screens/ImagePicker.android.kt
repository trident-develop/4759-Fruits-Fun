package org.example.project.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.example.project.AndroidContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberImagePickerLaunchers(onResult: (ByteArray?) -> Unit): ImagePickerLaunchers {
    val context = LocalContext.current
    val permissionDenied = remember { mutableStateOf<PermissionDeniedType?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (bitmap != null) {
                    val scaled = scaleBitmap(bitmap, 300)
                    val baos = ByteArrayOutputStream()
                    scaled.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                    onResult(baos.toByteArray())
                }
            } catch (_: Exception) {
                onResult(null)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            showCamera = true
        } else {
            val activity = context as? Activity
            if (activity != null &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)
            ) {
                permissionDenied.value = PermissionDeniedType.CAMERA_RATIONALE
            } else {
                permissionDenied.value = PermissionDeniedType.CAMERA_SETTINGS
            }
        }
    }

    // CameraX full-screen overlay
    if (showCamera) {
        Dialog(
            onDismissRequest = { showCamera = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
            ),
        ) {
            CameraScreen(
                onResult = { bytes ->
                    showCamera = false
                    onResult(bytes)
                },
                onDismiss = { showCamera = false },
            )
        }
    }

    return remember {
        ImagePickerLaunchers(
            launchGallery = {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            launchCamera = {
                when {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED -> {
                        showCamera = true
                    }
                    (context as? Activity)?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
                    } == true -> {
                        permissionDenied.value = PermissionDeniedType.CAMERA_RATIONALE
                    }
                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            permissionDenied = permissionDenied,
            retryPermission = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
        )
    }
}

actual fun openAppSettings() {
    val context = AndroidContext.context
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

internal fun scaleBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
    val ratio = minOf(maxSize.toFloat() / bitmap.width, maxSize.toFloat() / bitmap.height, 1f)
    val w = (bitmap.width * ratio).toInt()
    val h = (bitmap.height * ratio).toInt()
    return Bitmap.createScaledBitmap(bitmap, w, h, true)
}
