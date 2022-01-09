//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_SHADERUTILS_H
#define ANDROIDAPP_SHADERUTILS_H

#include <GLES3/gl3.h>
#include "Log.h"

class ShaderUtils {
public:
    static constexpr int INVALID_HANDLE = 0;

    /**
     * 创建并返回一个 shader program 句柄
     *
     * 这个函数会主动将创建并使用完的 shader 释放掉
     *
     * @param vertexShaderSource        顶点着色器代码
     * @param fragmentShaderSource      片段着色器代码
     * */
    static unsigned int
    CreateProgram(const char *vertexShaderSource, const char *fragmentShaderSource);

    /**
     * 创建并返回一个 shader program 句柄
     *
     * 调用者需要手动释放 vertexShader 和 fragmentShader
     *
     * @param vertexShader              顶点着色器句柄
     * @param fragmentShader            片段着色器句柄
     * */
    static unsigned int
    CreateProgram(const unsigned int vertexShader, const unsigned int fragmentShader);

    /**
     * 加载并返回一个 shader 句柄
     *
     * @param source                    着色器代码
     * @param type                      着色器类型
     * */
    static unsigned int LoadShader(const char *source, GLenum type);

private:
    // 错误的最大打印次数
    static constexpr int MAX_ERROR = 10;

    inline static const char *parseForString(int key) {
        switch (key) {
            case GL_VERTEX_SHADER:
                return "";
            default:
                return "";
        }
    }

    /**
     * 检查操作是否发生异常
     *
     * @param operation                 某项操作，仅用作 log 打印
     * */
    static void checkError(const char *operation);
};


#endif //ANDROIDAPP_SHADERUTILS_H
