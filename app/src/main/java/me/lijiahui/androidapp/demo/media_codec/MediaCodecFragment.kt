package me.lijiahui.androidapp.demo.media_codec

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.MediaCodec
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.lijiahui.androidapp.databinding.FragmentMediaCodecBinding
import me.lijiahui.androidapp.util.CameraUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MediaCodecFragment : Fragment() {

    companion object {
        private const val TAG = "MediaCodecFragment"

        private const val RECORDER_VIDEO_BITRATE = 10_100_100
        private const val RECORDER_VIDEO_FPS = 30
        private val RECORDER_VIDEO_SIZE = Size(1080, 2400)

        private val PERMISSIONS_REQUIRED =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        private const val PERMISSIONS_REQUEST_CODE = 0x800002

        private fun createVideoFile(context: Context, extension: String): File {
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
            return File(context.filesDir, "VID_${sdf.format(Date())}.$extension")
        }
    }

    private var _binding: FragmentMediaCodecBinding? = null

    private val mBinding get() = _binding!!

    private val mCameraManager by lazy {
        requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private var mCameraId = "0"

    private var mCameraDevice: CameraDevice? = null

    private var mCameraSession: CameraCaptureSession? = null

    private val recorderSurface: Surface by lazy {
        val surface = MediaCodec.createPersistentInputSurface()
        createRecorder(surface).apply {
            prepare()
            release()
        }
        surface
    }

    private val recorder: MediaRecorder by lazy {
        createRecorder(recorderSurface)
    }

    private val videoFile: File by lazy {
        createVideoFile(requireContext(), "mp4")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaCodecBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "screen size: ${CameraUtils.getDisplaySize(mBinding.viewFinder.display)}")
        Log.d(
            TAG,
            "preview size will be ${
                CameraUtils.choosePreviewSize(
                    requireContext(),
                    "0",
                    mBinding.viewFinder.display
                )
            }"
        )
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recorder.release()
        recorderSurface.release()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    mBinding.openCamera.callOnClick()
                } else {
                    Log.w(TAG, "user not give all permissions")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initView() {
        mBinding.viewFinder.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val previewSize = CameraUtils.choosePreviewSize(
                    requireContext(),
                    mCameraId,
                    mBinding.viewFinder.display
                )
                Log.d(TAG, "preview size: $previewSize")
                mBinding.viewFinder.setAspectRatio(previewSize.width, previewSize.height)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })

        mBinding.openCamera.apply {
            setOnClickListener {
                if (!checkPermissions(requireContext(), PERMISSIONS_REQUIRED)) {
                    requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
                    return@setOnClickListener
                }
                val previewSurface = mBinding.viewFinder.holder.surface
                val targets = listOf<Surface>(previewSurface)
                Log.d(TAG, "recorder surface")
                if (mCameraDevice != null || mCameraSession != null) {
                    Log.d(TAG, "should release origin camera device/session before start new one")
                    mCameraSession?.close()
                    mCameraDevice?.close()
                    mCameraDevice = null
                    mCameraSession = null
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    mCameraDevice = CameraUtils.openCamera(mCameraId, mCameraManager)
                    mCameraDevice?.let {
                        mCameraSession = CameraUtils.createCaptureSession(it, targets).apply {
                            val previewRequest =
                                it.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                                    addTarget(previewSurface)
                                }.build()
                            setRepeatingRequest(previewRequest, null, null)
                        }
                    }
                }
            }
        }
        mBinding.closeCamera.apply {
            setOnClickListener {
                mCameraSession?.close()
                mCameraDevice?.close()
                mCameraSession = null
                mCameraDevice = null
            }
        }
        mBinding.startRecord.apply {
            setOnClickListener {
                if (mCameraDevice == null || mCameraSession == null) {
                    Log.d(TAG, "camera device or session null! can not record")
                    return@setOnClickListener
                }
                mCameraDevice?.let {
                    val recordingRequest =
                        it.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
                            addTarget(mBinding.viewFinder.holder.surface)
                            addTarget(recorderSurface)
                        }.build()
                    mCameraSession?.let { session ->
                        session.setRepeatingRequest(recordingRequest, null, null)
                        recorder.apply {
                            prepare()
                            start()
                        }
                    }
                }
            }
        }

        mBinding.stopRecord.apply {
            setOnClickListener {
                recorder.stop()
                mCameraDevice?.let {
                    val previewRequest =
                        it.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                            addTarget(mBinding.viewFinder.holder.surface)
                        }.build()
                    mCameraSession?.setRepeatingRequest(previewRequest, null, null)
                }
            }
        }
    }

    private fun createRecorder(surface: Surface) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setVideoSource(MediaRecorder.VideoSource.CAMERA)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(videoFile.absolutePath)
        setVideoEncodingBitRate(RECORDER_VIDEO_BITRATE)
        setVideoFrameRate(RECORDER_VIDEO_FPS)
        setVideoSize(RECORDER_VIDEO_SIZE.width, RECORDER_VIDEO_SIZE.height)
        setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setInputSurface(surface)
    }

    private fun checkPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
}