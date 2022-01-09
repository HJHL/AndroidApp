package me.lijiahui.androidapp.widget

import android.util.Log
import java.nio.ByteBuffer


class MyNativeRender {
    companion object {
        private const val TAG = "MyNativeRender"
        private const val NATIVE_LIB_NAME = "native_render"
    }

    init {
        try {
            System.loadLibrary(NATIVE_LIB_NAME)
        } catch (e: UnsatisfiedLinkError) {
            Log.d(TAG, "link native lib error")
        }
    }

    external fun native_OnInit()
    external fun native_OnUninit()
    external fun native_OnSurfaceCreated()
    external fun native_OnSurfaceChanged(width: Int, height: Int)
    external fun native_OnDrawFrame()
    external fun native_setImageData(format: Int, width: Int, height: Int, bytes: ByteArray)
}