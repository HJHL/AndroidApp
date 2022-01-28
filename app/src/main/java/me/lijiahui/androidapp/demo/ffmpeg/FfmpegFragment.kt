package me.lijiahui.androidapp.demo.ffmpeg

import android.os.Bundle
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

    private fun initView() {
        mBinding.tvInfoDisplay.apply {
            text = native_getInfo()
        }
    }

    companion object {
        private const val TAG = "FfmpegFragment"
        private const val JNI_LIB_NAME = "jni_ffmpeg"

        init {
            System.loadLibrary(JNI_LIB_NAME)
        }
    }
}