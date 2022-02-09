package me.lijiahui.androidapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.Surface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CameraUtils {

    private const val TAG = "CameraUtils"

    /**
     * 获取屏幕真实尺寸
     * */
    fun getDisplaySize(display: Display): Size {
        val outputPoint = Point()
        display.getRealSize(outputPoint)
        return Size(outputPoint.y, outputPoint.x)
    }

    /**
     * 找到合适的预览尺寸
     *
     * 所谓合适，指最接近屏幕物理尺寸的那个预览尺寸
     * */
    fun choosePreviewSize(context: Context, cameraId: String, display: Display): Size {
        val displaySize = getDisplaySize(display)
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristic = manager.getCameraCharacteristics(cameraId)
        val streamConfigurationMap =
            characteristic[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
        streamConfigurationMap?.let {
            val previewSupportedSizes =
                streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
            val validSizes =
                previewSupportedSizes.sortedWith(compareBy { it.height * it.width }).reversed()
            return validSizes.first { it.width <= displaySize.width && it.height <= displaySize.height }
        }
        return Size(1080, 1920)
    }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun openCamera(
        id: String,
        manager: CameraManager,
        handler: Handler? = null
    ): CameraDevice =
        suspendCancellableCoroutine { cont ->
            manager.openCamera(id, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) = cont.resume(camera)

                override fun onDisconnected(camera: CameraDevice) {
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    val msg = when (error) {
                        ERROR_CAMERA_SERVICE -> "Fatal (service)"
                        ERROR_CAMERA_DEVICE -> "Fatal (device)"
                        ERROR_CAMERA_IN_USE -> "Camera in use"
                        ERROR_CAMERA_DISABLED -> "Camera disable"
                        ERROR_MAX_CAMERAS_IN_USE -> "Maximum camera in use"
                        else -> "Unknown"
                    }
                    val exc = RuntimeException("Camera $id error: ($error) $msg")
                    Log.d(TAG, exc.message, exc)
                    if (cont.isActive) cont.resumeWithException(exc)
                }
            }, handler)
        }

    suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->
        device.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cont.resume(session)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                Log.d(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }, handler)
    }
}