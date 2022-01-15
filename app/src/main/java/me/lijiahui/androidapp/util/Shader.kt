package me.lijiahui.androidapp.util

import android.opengl.GLES30.*
import android.util.Log


class Shader(vertexCode: String, fragmentCode: String) {
    companion object {
        private const val TAG = "Shader"
    }

    var program: Int = 0
        private set

    init {
        val vertex = OpenGLUtils.createShader(GL_VERTEX_SHADER, vertexCode)
        val fragment = OpenGLUtils.createShader(GL_FRAGMENT_SHADER, fragmentCode)
        program = OpenGLUtils.createProgram(vertex, fragment)
        Log.i(TAG, "vertex $vertex fragment $fragment program $program")
    }

    fun use() {
        glUseProgram(program)
    }

    fun setInt(name: String, value: Int) {
        val location = getLocation(name)
        glUniform1i(location, value)
    }

    fun getLocation(name: String): Int {
        val location = glGetUniformLocation(program, name)
        Log.d(TAG, "[$name] location $location")
        return location
    }
}