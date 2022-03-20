package me.lijiahui.androidapp.demo.screen_recorder

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.lijiahui.androidapp.MyApplication
import me.lijiahui.androidapp.databinding.FragmentScreenRecorderBinding
import me.lijiahui.androidapp.util.PermissionUtils
import me.lijiahui.androidapp.util.ToastUtils

/**
 * 录屏
 * */
class ScreenRecorderFragment : Fragment() {

    companion object {
        private const val TAG = "ScreenRecorderFragment"
        private val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
        )
        private const val REQUEST_CODE_PERMISSIONS = 0x800001
        private const val REQUEST_CODE_SCREEN_RECORDER = 0x800002
    }

    private var _binding: FragmentScreenRecorderBinding? = null
    private val mBinding get() = _binding!!

    private val mMediaProjectionManager by lazy {
        MyApplication.app.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private var mEncoder: MediaCodec? = null

    private var mResultCode: Int = RESULT_OK
    private var mResultIntent: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenRecorderBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                requestScreenRecording()
            }
            REQUEST_CODE_SCREEN_RECORDER -> {
                if (resultCode == RESULT_CANCELED || data == null) {
                    ToastUtils.showShort("screen record not granted")
                }
                startScreenRecording()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        mBinding.startRecord.apply {
            setOnClickListener {
                requestScreenRecording()
            }
        }
    }

    private fun requestScreenRecording() {
        if (PermissionUtils.checkPermissions(PERMISSIONS)) {
            startActivityForResult(
                mMediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_CODE_SCREEN_RECORDER
            )
        } else {
            requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startScreenRecording() {
        if (mEncoder == null) {
            mEncoder = createEncoder()
        }
        mResultIntent?.let {
            val mediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, it)
            mediaProjection.createVirtualDisplay(
                "screen_recorder",
                1080,
                1920,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mEncoder!!.createInputSurface(),
                null, null
            )
        }
    }

    private fun createEncoder(): MediaCodec = MediaCodec.createEncoderByType("video/avc").also {
        val format = MediaFormat.createVideoFormat("video/avc", 1080, 1920).apply {
            setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            )
            setInteger(MediaFormat.KEY_BIT_RATE, 1024 * 1000)
            setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10)
            setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000/45)
            setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR)
            setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR)
        }
        try {
            it.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        } catch (e: Exception) {
            Log.d(TAG, e.message, e)
            it.reset()
            it.stop()
            it.release()
        }
    }
}