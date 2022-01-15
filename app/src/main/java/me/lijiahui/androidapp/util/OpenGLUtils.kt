package me.lijiahui.androidapp.util

import android.opengl.GLES30
import android.util.Log
import java.nio.IntBuffer

object OpenGLUtils {
    private const val TAG = "OpenGLUtils"
    private const val DEBUG = true

    fun createShader(type: Int, code: String): Int {
        val shaderType = when (type) {
            GLES30.GL_VERTEX_SHADER -> "Vertex Shader"
            GLES30.GL_FRAGMENT_SHADER -> "Fragment Shader"
            else -> "Unsupported type"
        }
        if (DEBUG) {
            Log.d(TAG, "create [$shaderType] code $code")
        }
        if (type != GLES30.GL_VERTEX_SHADER && type != GLES30.GL_FRAGMENT_SHADER) {
            Log.w(TAG, "create shader failed: unsupported shader type, your input $type")
        }
        return GLES30.glCreateShader(type).also {
            GLES30.glShaderSource(it, code)
            GLES30.glCompileShader(it)
            if (DEBUG) {
                // check shader compile status
                val infoLengthBuffer = IntBuffer.allocate(1)
                GLES30.glGetShaderiv(it, GLES30.GL_INFO_LOG_LENGTH, infoLengthBuffer)
                Log.d(TAG, "compile [$shaderType] ${GLES30.glGetShaderInfoLog(it)}")
                if (infoLengthBuffer[0] == 0) {
                    Log.d(TAG, "compile [$shaderType] success!")
                } else {
                    Log.w(
                        TAG,
                        "compile [$shaderType] failed! info: ${GLES30.glGetShaderInfoLog(it)}"
                    )
                }
            }
        }
    }

    fun createProgram(vertex: Int, fragment: Int): Int = GLES30.glCreateProgram().also {
        GLES30.glAttachShader(it, vertex)
        GLES30.glAttachShader(it, fragment)
        GLES30.glLinkProgram(it)
        if (DEBUG) {
            val infoLengthBuffer = IntBuffer.allocate(1)
            GLES30.glGetProgramiv(it, GLES30.GL_INFO_LOG_LENGTH, infoLengthBuffer)
            if (infoLengthBuffer[0] == 0) {
                Log.d(TAG, "link program success!")
            } else {
                Log.w(
                    TAG,
                    "link program failed! info: ${GLES30.glGetProgramInfoLog(it)}"
                )
            }
        }
    }
}