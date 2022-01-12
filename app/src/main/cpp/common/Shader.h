//
// Created by Joe.Lee on 2022/1/12.
//

#ifndef ANDROIDAPP_SHADER_H
#define ANDROIDAPP_SHADER_H

#include <string>
#include <GLES3/gl3.h>
#include "Log.h"
#include "third_party/glm/glm.hpp"

class Shader {
public:
    Shader(const std::string &vertexShaderFile,
           const std::string &fragmentShaderFile);

    /**
     * 使用 shader
     * */
    inline void use() const {
        glUseProgram(m_program);
    }

    /**
     * 获取 uniform 变量的句柄
     *
     * @param name          uniform 变量的名字
     * */
    int getLocation(const std::string &name) const {
        if (m_program == 0) {
            ALOGW("invalid OpenGL program handle");
            return INVALID_HANDLE;
        }
        const int location = glGetUniformLocation(m_program, name.c_str());
        if (DEBUG) {
            ALOGD("%s's position in program %d", name.c_str(), location);
        }
        return location;
    }

    /**
     * 获取 shader 的句柄
     * */
    const unsigned int getProgram() const {
        return m_program;
    }

    inline void setBool(const std::string &name, const bool value) const {
        const int location = getLocation(name);
        glUniform1i(location, (int) value);
    }

    inline void setInt(const std::string &name, const int value) const {
        const int location = getLocation(name);
        glUniform1i(location, value);
    }

    inline void setFloat(const std::string &name, const float value) const {
        const int location = getLocation(name);
        glUniform1f(location, value);
    }

    inline void setVec2(const std::string &name, const glm::vec2 &value) const {
        const int location = getLocation(name);
        glUniform2fv(location, 1, &value[0]);
    }

    inline void setVec2(const std::string &name,
                        const float x, const float y) const {
        const int location = getLocation(name);
        glUniform2f(location, x, y);
    }

    inline void setVec3(const std::string &name, const glm::vec3 &value) const {
        const int location = getLocation(name);
        glUniform3fv(location, 1, &value[0]);
    }

    inline void setVec3(const std::string &name,
                        const float x, const float y, const float z) const {
        const int location = getLocation(name);
        glUniform3f(location, x, y, z);
    }

    inline void setVec4(const std::string &name, const glm::vec4 &value) const {
        const int location = getLocation(name);
        glUniform4fv(location, 1, &value[0]);
    }

    inline void setVec4(const std::string &name,
                        const float x, const float y, const float z, const float w) const {
        const int location = getLocation(name);
        glUniform4f(location, x, y, z, w);
    }

    inline void setMat2(const std::string &name, const glm::mat2 &value) const {
        const int location = getLocation(name);
        glUniformMatrix2fv(location, 1, GL_FALSE, &value[0][0]);
    }

    inline void setMat3(const std::string &name, const glm::mat3 &value) const {
        const int location = getLocation(name);
        glUniformMatrix3fv(location, 1, GL_FALSE, &value[0][0]);
    }

    inline void setMat4(const std::string &name, const glm::mat4 &value) const {
        const int location = getLocation(name);
        glUniformMatrix4fv(location, 1, GL_FALSE, &value[0][0]);
    }

private:
    static const int INVALID_HANDLE = -1;
    static const bool DEBUG = false;

    /**
     * 初始化 shader program
     *
     * @param vertexShaderCode          顶点着色器源码
     * @param fragmentShaderCode        片段着色器源码
     * */
    int init(const char *const vertexShaderCode, const char *const fragmentShaderCode);

    unsigned int m_program;
};


#endif //ANDROIDAPP_SHADER_H
