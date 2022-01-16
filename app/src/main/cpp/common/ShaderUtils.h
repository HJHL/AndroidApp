//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_SHADERUTILS_H
#define ANDROIDAPP_SHADERUTILS_H

#include <GLES3/gl3.h>
#include "Log.h"
#include <string>

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

    /**
     * 创建一个 2D texture，并将图片加载上去
     *
     * @param imagePath                 图片文件路径
     *
     * @return 返回 texture 的 handle
     * */
    static unsigned int LoadImageToTexture2D(const std::string &imagePath);

private:
    // 错误的最大打印次数
    static constexpr int MAX_ERROR = 10;

    /**
     * 检查操作是否发生异常
     *
     * @param operation                 某项操作，仅用作 log 打印
     * */
    static void checkError(const char *operation);

    /**
     * 查询 shader 信息
     *
     * @param shader                    shader 的句柄
     * @param type                      检查类型，当前仅确保支持 [GL_COMPILE_STATUS]
     * @param operation                 检查操作信息的说明字段，仅打 log 用
     * */
    static void checkShaderInfo(const unsigned int shader,
                                const int type, const std::string &operation);

    /**
     * 查询程序信息
     *
     * @param program                   program 的句柄
     * @param type                      检查类型，当前仅确保支持 [GL_LINK_STATUS]
     * @param operation                 检查操作信息的说明字段，仅打 log 用
     * */
    static void checkProgramInfo(const unsigned int program,
                                 const int type, const std::string &operation);

private:
    enum CheckType {
        Shader,
        Program,
    };

    /**
     * 检查 shader 代码的执行信息
     * 如：
     *  1. 程序是否链接成功，如果失败，打印出失败信息
     *  2. shader 是否编译成功，如果失败，打印出失败信息
     * */
    static void checkInfo(const unsigned int handle,
                          const int type, const std::string &operation, CheckType checkType);
};


#endif //ANDROIDAPP_SHADERUTILS_H
