package me.lijiahui.androidapp.demo.ffmpeg

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.lijiahui.androidapp.databinding.FragmentFfmpegBinding

class FfmpegFragment : Fragment() {

    private var _binding: FragmentFfmpegBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFfmpegBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private external fun native_getInfo(): String

    private external fun native_getEncoderInfo(): String

    private external fun native_getDecoderInfo(): String

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mBinding.tvInfoDisplay.apply {
            text = "${native_getInfo()}\n" +
                    "Encoder info: ${native_getEncoderInfo()}\n" +
                    "Decoder info: ${native_getDecoderInfo()}"
            movementMethod = ScrollingMovementMethod()
        }
        mBinding.jumpToMarket.apply {
            val pkg = "com.gorgeous.lite"
            setOnClickListener {
                kotlin.runCatching {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
                }.onSuccess {

                }.onFailure {

                }
            }
        }
    }

    companion object {
        private const val TAG = "FfmpegFragment"
        private const val JNI_LIB_NAME = "jni_ffmpeg"

        init {
            try {
                System.loadLibrary(JNI_LIB_NAME)
            } catch (e: Exception) {
                Log.d(TAG, e.message, e)
            }
        }
    }
}