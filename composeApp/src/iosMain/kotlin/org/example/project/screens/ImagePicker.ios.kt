package org.example.project.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.Foundation.NSOperationQueue
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import platform.darwin.NSObject

// Strong references to delegates to prevent GC (ObjC delegate properties are weak)
private var currentGalleryDelegate: NSObject? = null
private var currentCameraDelegate: NSObject? = null

@Composable
actual fun rememberImagePickerLaunchers(onResult: (ByteArray?) -> Unit): ImagePickerLaunchers {
    val permissionDenied = remember { mutableStateOf<PermissionDeniedType?>(null) }

    return remember {
        ImagePickerLaunchers(
            launchGallery = { launchGalleryPicker(onResult, permissionDenied) },
            launchCamera = { launchCameraPicker(onResult, permissionDenied) },
            permissionDenied = permissionDenied,
        )
    }
}

actual fun openAppSettings() {
    val settingsUrl = platform.UIKit.UIApplicationOpenSettingsURLString
    val url = NSURL.URLWithString(settingsUrl) ?: return
    UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
}

// ── Gallery (with photo library permission check) ─────────────────────

private fun launchGalleryPicker(
    onResult: (ByteArray?) -> Unit,
    permissionDenied: MutableState<PermissionDeniedType?>,
) {
    when (PHPhotoLibrary.authorizationStatus()) {
        PHAuthorizationStatusAuthorized, PHAuthorizationStatusLimited ->
            presentGalleryPicker(onResult)

        PHAuthorizationStatusNotDetermined -> {
            PHPhotoLibrary.requestAuthorization { status ->
                NSOperationQueue.mainQueue.addOperationWithBlock {
                    if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited) {
                        presentGalleryPicker(onResult)
                    } else {
                        permissionDenied.value = PermissionDeniedType.GALLERY_SETTINGS
                    }
                }
            }
        }

        PHAuthorizationStatusDenied, PHAuthorizationStatusRestricted -> {
            permissionDenied.value = PermissionDeniedType.GALLERY_SETTINGS
        }

        else -> permissionDenied.value = PermissionDeniedType.GALLERY_SETTINGS
    }
}

private fun presentGalleryPicker(onResult: (ByteArray?) -> Unit) {
    val config = PHPickerConfiguration()
    config.filter = PHPickerFilter.imagesFilter
    config.selectionLimit = 1

    val picker = PHPickerViewController(configuration = config)
    val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            picker.dismissViewControllerAnimated(true, null)
            currentGalleryDelegate = null

            val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: run {
                onResult(null)
                return
            }
            result.itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, _ ->
                val nsData = data as? NSData
                if (nsData != null) {
                    val image = UIImage.imageWithData(nsData)
                    if (image != null) {
                        val scaled = scaleImage(image, 300.0)
                        val jpeg = UIImageJPEGRepresentation(scaled, 0.8)
                        if (jpeg != null) {
                            val bytes = jpeg.toByteArray()
                            NSOperationQueue.mainQueue.addOperationWithBlock { onResult(bytes) }
                        } else {
                            NSOperationQueue.mainQueue.addOperationWithBlock { onResult(null) }
                        }
                    } else {
                        NSOperationQueue.mainQueue.addOperationWithBlock { onResult(null) }
                    }
                } else {
                    NSOperationQueue.mainQueue.addOperationWithBlock { onResult(null) }
                }
            }
        }
    }
    currentGalleryDelegate = delegate
    picker.delegate = delegate

    getTopViewController()
        ?.presentViewController(picker, animated = true, completion = null)
}

// ── Camera (permission via AVCaptureDevice) ────────────────────────────

private fun launchCameraPicker(
    onResult: (ByteArray?) -> Unit,
    permissionDenied: MutableState<PermissionDeniedType?>,
) {
    if (!UIImagePickerController.isSourceTypeAvailable(
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera,
        )
    ) {
        onResult(null)
        return
    }

    when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> presentCamera(onResult)

        AVAuthorizationStatusNotDetermined -> {
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                NSOperationQueue.mainQueue.addOperationWithBlock {
                    if (granted) {
                        presentCamera(onResult)
                    } else {
                        permissionDenied.value = PermissionDeniedType.CAMERA_SETTINGS
                    }
                }
            }
        }

        AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> {
            permissionDenied.value = PermissionDeniedType.CAMERA_SETTINGS
        }

        else -> permissionDenied.value = PermissionDeniedType.CAMERA_SETTINGS
    }
}

private fun presentCamera(onResult: (ByteArray?) -> Unit) {
    val picker = UIImagePickerController()
    picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
    picker.allowsEditing = true

    val delegate = object : NSObject(),
        UIImagePickerControllerDelegateProtocol,
        UINavigationControllerDelegateProtocol {

        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>,
        ) {
            picker.dismissViewControllerAnimated(true, null)
            currentCameraDelegate = null
            val image = (didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
                ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]) as? UIImage
            if (image != null) {
                val scaled = scaleImage(image, 300.0)
                val jpeg = UIImageJPEGRepresentation(scaled, 0.8)
                if (jpeg != null) {
                    onResult(jpeg.toByteArray())
                } else onResult(null)
            } else onResult(null)
        }

        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            picker.dismissViewControllerAnimated(true, null)
            currentCameraDelegate = null
            onResult(null)
        }
    }
    currentCameraDelegate = delegate
    picker.delegate = delegate

    getTopViewController()
        ?.presentViewController(picker, animated = true, completion = null)
}

// ── Helpers ────────────────────────────────────────────────────────────

private fun getTopViewController(): UIViewController? {
    var topVC = org.example.project.ViewControllerHolder.viewController
    while (topVC?.presentedViewController != null) {
        topVC = topVC.presentedViewController
    }
    return topVC
}

@OptIn(ExperimentalForeignApi::class)
private fun scaleImage(image: UIImage, maxSize: Double): UIImage {
    var w = 0.0
    var h = 0.0
    image.size.useContents { w = width; h = height }
    val ratio = minOf(maxSize / w, maxSize / h, 1.0)
    val newW = w * ratio
    val newH = h * ratio
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(newW, newH), false, 1.0)
    image.drawInRect(CGRectMake(0.0, 0.0, newW, newH))
    val result = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return result ?: image
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    if (size == 0) return ByteArray(0)
    val bytes = ByteArray(size)
    bytes.usePinned { pinned ->
        platform.posix.memcpy(pinned.addressOf(0), this.bytes, size.toULong())
    }
    return bytes
}
