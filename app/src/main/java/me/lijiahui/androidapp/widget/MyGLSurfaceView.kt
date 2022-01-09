package me.lijiahui.androidapp.widget

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import androidx.annotation.Keep
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@Keep
class MyGLSurfaceView(context: Context, attributeSet: AttributeSet?) :
    GLSurfaceView(context, attributeSet), GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MyGLSurfaceView"
    }

    constructor(context: Context) : this(context, null)

    val nativeRender: MyNativeRender

    init {
        // 使用 OpenGL ES 3.0 环境
        setEGLContextClientVersion(3)
        // 使用 native render 进行渲染
        nativeRender = MyNativeRender()
        setRenderer(this)
        // 设置渲染模式为持续渲染模式
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        nativeRender.native_OnInit()
        nativeRender.native_OnSurfaceCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        nativeRender.native_OnSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        nativeRender.native_OnDrawFrame()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.surfaceDestroyed(holder)
        Log.d(TAG, "surfaceDestroyed")
        //nativeRender.native_OnUninit()
    }

    override fun finalize() {
        super.finalize()
        Log.d(TAG, "finalize")
        nativeRender.native_OnUninit()
    }
}