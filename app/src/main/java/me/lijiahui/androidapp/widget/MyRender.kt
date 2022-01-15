package me.lijiahui.androidapp.widget

import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import android.util.Log
import me.lijiahui.androidapp.MyApplication
import me.lijiahui.androidapp.util.OpenGLUtils
import java.nio.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRender : GLSurfaceView.Renderer {
    companion object {
        private const val TAG = "MyRender"
        private const val DEBUG = true
        private const val VERTEX_CODE_PATH = "vertex.glsl"
        private const val FRAGMENT_CODE_PATH = "fragment.glsl"
        private const val FLOAT_TYPE_SIZE = 4
        private const val SHORT_TYPE_SIZE = 2
    }

    private var program: Int = 0
    private var VAO: Int = 0

    init {
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.d(TAG, "onSurfaceCreated")
        setup()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.d(TAG, "onSurfaceChanged ${width}x$height")
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (DEBUG) {
            //Log.d(TAG, "onDrawFrame")
        }
        glClear(GL_COLOR_BUFFER_BIT)
        useProgram()
        glBindVertexArray(VAO)
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0)
    }

    private fun checkGlError(what: String = "") {
        Log.d(TAG, "checkGlError code [$what] ${glGetError()}")
    }

    private fun useProgram() {
        glUseProgram(program)
    }


    private fun setup() {
        val vertexCode: String =
            MyApplication.app.assets.open(VERTEX_CODE_PATH).bufferedReader().use {
                it.readText()
            }
        val fragmentCode: String =
            MyApplication.app.assets.open(FRAGMENT_CODE_PATH).bufferedReader().use {
                it.readText()
            }
        val vertex = OpenGLUtils.createShader(GL_VERTEX_SHADER, vertexCode)
        val fragment = OpenGLUtils.createShader(GL_FRAGMENT_SHADER, fragmentCode)
        // 创建程序
        program = OpenGLUtils.createProgram(vertex, fragment)
        Log.i(TAG, "vertex $vertex fragment $fragment program $program")
        // 释放资源
        glDeleteShader(vertex)
        glDeleteShader(fragment)

        // 白色作为背景色
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        val vertices = floatArrayOf(
            // 顶点坐标（3），颜色（3），纹理坐标（2）
            -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 左下
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // 左上
            1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, // 右上
            1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, // 右下
        )
        val indices = shortArrayOf(
            // 第一个三角形
            0, 1, 2,
            // 第二个三角形
            0, 2, 3,
        )
        val verticesBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(vertices.size * FLOAT_TYPE_SIZE).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(vertices)
                    position(0)
                }
            }
        val indicesBuffer: ShortBuffer =
            ByteBuffer.allocateDirect(indices.size * SHORT_TYPE_SIZE).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(indices)
                    position(0)
                }
            }

        val vao = IntBuffer.allocate(1)
        val vbo = IntBuffer.allocate(1)
        val ebo = IntBuffer.allocate(1)
        VAO = vao[0]
        glGenVertexArrays(vao.capacity(), vao)
        glGenBuffers(vbo.capacity(), vbo)
        glGenBuffers(ebo.capacity(), ebo)
        Log.d(TAG, "vao ${vao[0]} vbo ${vbo[0]} ebo ${ebo[0]}")

        glBindVertexArray(vao[0])

        glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
        glBufferData(GL_ARRAY_BUFFER, vertices.size, verticesBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo[0])
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size, indicesBuffer, GL_STATIC_DRAW)


        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_TYPE_SIZE, 0)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_TYPE_SIZE, 3)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_TYPE_SIZE, 6)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)
        checkGlError()
    }
}