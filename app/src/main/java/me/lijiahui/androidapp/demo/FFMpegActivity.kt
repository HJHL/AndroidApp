package me.lijiahui.androidapp.demo

import android.graphics.RectF
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import me.lijiahui.androidapp.databinding.ActivityFfmpegBinding

class FFMpegActivity : AppCompatActivity() {

    companion object {
        private const val FFMPEG_JNI_LIB_NAME = "jni"
        private const val TAG = "FFMpegActivity"

        init {
            try {
                System.loadLibrary(FFMPEG_JNI_LIB_NAME)
                Log.i(TAG, "success load ffmpeg jni lib")
            } catch (e: Exception) {
                Log.e(TAG, "load ffmpeg jni interface failed")
            }
        }
    }

    private var _binding: ActivityFfmpegBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFfmpegBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.tvMmpegEncodeInfo.apply {
            text = native_encodec_info()
            movementMethod = ScrollingMovementMethod()
        }
        mBinding.tvMmpegDecodeInfo.apply {
            text = native_decodec_info()
            movementMethod = ScrollingMovementMethod()
        }
        mBinding.gridLine.post {
            mBinding.gridLine.mDrawBounds = RectF(
                0.0F,
                0.0F,
                mBinding.gridLine.width.toFloat(),
                mBinding.gridLine.height.toFloat()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private external fun native_encodec_info(): String

    private external fun native_decodec_info(): String
}