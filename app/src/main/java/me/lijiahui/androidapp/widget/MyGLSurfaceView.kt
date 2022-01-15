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
    val myRender: MyRender

    var ratioWidth = 0
        private set
    var ratioHeight = 0
        private set

    init {
        // 使用 OpenGL ES 3.0 环境
        setEGLContextClientVersion(3)
        // 使用 native render 进行渲染
        nativeRender = MyNativeRender()
        myRender = MyRender()
        setRenderer(this)
        // 设置渲染模式为持续渲染模式
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    /**
     * 修改 SurfaceView 大小
     *
     * @param width         修改后的宽
     * @param height        修改后的高
     * */
    fun setAspectRatio(width: Int, height: Int) {
        Log.d(TAG, "setAspectRatio old ${ratioWidth}x$ratioHeight -> new ${width}x$height")
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative")
        }
        ratioWidth = width
        ratioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(
            TAG,
            "onMeasure input ${widthMeasureSpec}x$heightMeasureSpec ratio ${ratioWidth}x$ratioHeight"
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (ratioWidth == 0 || ratioHeight == 0) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * ratioWidth / ratioHeight) {
                setMeasuredDimension(width, width * ratioHeight / ratioWidth)
            } else {
                setMeasuredDimension(height * ratioWidth / ratioHeight, height)
            }
        }
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
//        nativeRender.native_OnUninit()
    }

    override fun finalize() {
        super.finalize()
        Log.d(TAG, "finalize")
        nativeRender.native_OnUninit()
    }
}