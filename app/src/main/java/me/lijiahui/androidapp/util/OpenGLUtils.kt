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
            Log.w(TAG, "create shader maybe failed: unsupported shader type, your input $type")
        }
        return GLES30.glCreateShader(type).also {
            GLES30.glShaderSource(it, code)
            GLES30.glCompileShader(it)
            if (DEBUG) {
                // check shader compile status
                val compileStatus = IntBuffer.allocate(1)
                GLES30.glGetShaderiv(it, GLES30.GL_COMPILE_STATUS, compileStatus)
                Log.d(
                    TAG,
                    "compile [$shaderType] status: ${if (compileStatus[0] == GLES30.GL_TRUE) "success" else "failed"}"
                )
                if (compileStatus[0] == GLES30.GL_FALSE) {
                    Log.d(TAG, "compile [$shaderType] ${GLES30.glGetShaderInfoLog(it)}")
                }
            }
        }
    }

    fun createProgram(vertex: Int, fragment: Int): Int = GLES30.glCreateProgram().also {
        if (vertex <= 0 || fragment <= 0) {
            Log.w(TAG, "invalid argument: vertex $vertex, fragment $fragment")
        }
        GLES30.glAttachShader(it, vertex)
        GLES30.glAttachShader(it, fragment)
        GLES30.glLinkProgram(it)
        if (DEBUG) {
            val linkStatus = IntBuffer.allocate(1)
            GLES30.glGetProgramiv(it, GLES30.GL_LINK_STATUS, linkStatus)
            if (linkStatus[0] == GLES30.GL_TRUE) {
                Log.d(TAG, "link program success!")
            } else {
                Log.e(
                    TAG,
                    "link program failed! info: ${GLES30.glGetProgramInfoLog(it)}"
                )
            }
        }
    }

    fun checkGLError(what: String) {
        Log.w(TAG, "[$what] error code: ${GLES30.glGetError()}")
    }
}