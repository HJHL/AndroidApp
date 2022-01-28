package me.lijiahui.androidapp.demo.floating

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.fragment.app.Fragment
import me.lijiahui.androidapp.databinding.FragmentFloatingBinding

class FloatingFragment : Fragment() {

    private var _binding: FragmentFloatingBinding? = null
    private val mBinding get() = _binding!!

    private var floatingView: FloatWindowView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloatingBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        mBinding.openFloating.setOnClickListener {
            if (!FloatingWindowsManager.checkFloatingWindowPermission(requireContext())) {
                FloatingWindowsManager.requestFloatingPermission(requireContext())
            }
            val lp = WindowManager.LayoutParams().apply {
                width = WRAP_CONTENT
                height = WRAP_CONTENT
                flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//                } else {
//                    type = WindowManager.LayoutParams.TYPE_PHONE
//                }
            }
            floatingView = FloatWindowView(requireContext())
            requireActivity().windowManager.addView(floatingView, lp)
        }
        mBinding.closeFloating.setOnClickListener {
        }
    }
}