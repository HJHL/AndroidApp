package me.lijiahui.androidapp.demo.hideapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.lijiahui.androidapp.Constants
import me.lijiahui.androidapp.R

class HideApiFragment : Fragment() {
    private var mHideApiAccess: Boolean = false
    private lateinit var mTvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mHideApiAccess = it.getBoolean(ARG_ENABLE_HIDE_API_ACCESS, false)
        }
        Log.d(TAG, "enable hide api access: $mHideApiAccess")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_hide_api, container, false)
        mTvInfo = view.findViewById(R.id.tv_display)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tryAccessHideApi()
    }

    @SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi")
    private fun tryAccessHideApi() {
        try {
            val vmRuntimeClass = Class.forName(Constants.CLASS_NAME_VM_RUNTIME)
            val getRuntime = vmRuntimeClass.getDeclaredMethod("getRuntime").invoke(null)
            val method = vmRuntimeClass.getDeclaredMethod("getTargetSdkVersion")
            val result = method.invoke(getRuntime) as? Int ?: -1
            Log.d(TAG, "result $result")
        } catch (e: Exception) {
            Log.w(TAG, e.message, e)
        }
    }

    companion object {
        private const val TAG = "HideApiFragment"
        private const val ARG_ENABLE_HIDE_API_ACCESS = "enable_hidden_api_access"
        fun newInstance(enableHideApiAccess: Boolean) =
            HideApiFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_ENABLE_HIDE_API_ACCESS, enableHideApiAccess)
                }
            }

        init {
            System.loadLibrary("access_hide_api")
        }
    }
}