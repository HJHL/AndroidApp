//
// Created by Joe.Lee on 2022/1/8.
//
#define LOG_TAG "ShaderUtils"

#include "ShaderUtils.h"
#include <malloc.h>

// 异常时（如图像没有绘制），取消掉下面注释的开关
// 线上环境禁止打开，因为 OpenGL 异常查询会让 CPU 与 GPU 间进行通信，频繁地进行会严重损耗性能
//#define DEBUG

unsigned int ShaderUtils::LoadShader(const char *source, GLenum type) {
    if (source == nullptr) {
        ALOGE("the shader source code is null!!!");
        return INVALID_HANDLE;
    }
    if (type != GL_VERTEX_SHADER && type != GL_FRAGMENT_SHADER) {
        ALOGE("invalid args! pls check your shader type");
        return INVALID_HANDLE;
    }
    unsigned int shaderHandle = INVALID_HANDLE;
    shaderHandle = glCreateShader(type);
    if (shaderHandle == INVALID_HANDLE) {
        ALOGE("create shader failed! maybe OpenGL environment not setup");
        return INVALID_HANDLE;
    }
    glShaderSource(shaderHandle, 1, &source, nullptr);
    glCompileShader(shaderHandle);
#ifdef DEBUG
    // 检查 glCompileShader 状态
    // 建议当 shaderHandle 为 0 时打开，一般是 source 代码存在问题，或者是 type 类型不对
    GLint compiledStatus = 0;
    glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, &compiledStatus);
    if (!compiledStatus) {
        ALOGE("compile shader failed");
        GLint msgLength = 0;
        glGetShaderiv(shaderHandle, GL_INFO_LOG_LENGTH, &msgLength);
        if (msgLength) {
            char *buf = (char *) malloc((size_t) msgLength);
            if (buf) {
                glGetShaderInfoLog(shaderHandle, msgLength, nullptr, buf);
                ALOGE("compile shader info %s", buf);
                free(buf);
                buf = nullptr;
            }
        } else {
            ALOGE("get info length failed");
        }
    }
#endif
    return shaderHandle;
}

unsigned int ShaderUtils::CreateProgram(const unsigned int vertexShader,
                                        const unsigned int fragmentShader) {
    if (vertexShader == 0 || fragmentShader == 0) return INVALID_HANDLE;
    unsigned int programHandle = INVALID_HANDLE;
    programHandle = glCreateProgram();
    if (programHandle == INVALID_HANDLE) {
        ALOGE("create program failed, maybe OpenGL environment not setup!");
        return INVALID_HANDLE;
    }
    glAttachShader(programHandle, vertexShader);
#ifdef DEBUG
    // 检查 glAttachShader 状态
    checkError("glAttachShader");
#endif
    glAttachShader(programHandle, fragmentShader);
#ifdef DEBUG
    // 检查 glAttachShader 状态
    checkError("glAttachShader");
#endif
    glLinkProgram(programHandle);
#ifdef DEBUG
    // 检查 glLinkProgram 状态
    GLint linkStatus = 0;
    glGetProgramiv(programHandle, GL_LINK_STATUS, &linkStatus);
    if (linkStatus != GL_TRUE) {
        GLint msgLength = 0;
        glGetProgramiv(programHandle, GL_INFO_LOG_LENGTH, &msgLength);
        if (msgLength) {
            char *msgBuffer = (char *) malloc((size_t) msgLength);
            if (msgBuffer) {
                glGetProgramInfoLog(programHandle, msgLength, nullptr, msgBuffer);
                ALOGE("link program failed, msg: %d", msgBuffer);
                free(msgBuffer);
                msgBuffer = nullptr;
            }
        } else {
            ALOGE("get link info log length failed");
        }
    }
#endif
    return programHandle;
}

unsigned int ShaderUtils::CreateProgram(const char *vertexShaderSource,
                                        const char *fragmentShaderSource) {
    unsigned int vertexShaderHandle = LoadShader(vertexShaderSource, GL_VERTEX_SHADER);
    if (vertexShaderHandle == INVALID_HANDLE) {
        ALOGI("load vertex shader failed");
        return INVALID_HANDLE;
    }
    unsigned int fragmentShaderHandle = LoadShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
    if (fragmentShaderHandle == INVALID_HANDLE) {
        ALOGI("load fragment shader failed");
        return INVALID_HANDLE;
    }
    unsigned int programHandle = CreateProgram(vertexShaderHandle, fragmentShaderHandle);
#ifdef DEBUG
    ALOGI("check vertex %d fragment %d program handle %d",
          vertexShaderHandle, fragmentShaderHandle, programHandle);
#endif
    // 完成 Attach 和 Link 后，vertexShaderHandle 和 fragmentShaderHandle 就没啥用了，可以释放掉
    glDeleteShader(vertexShaderHandle);
    glDeleteShader(fragmentShaderHandle);
    vertexShaderHandle = 0;
    fragmentShaderHandle = 0;
    return programHandle;
}

void ShaderUtils::checkError(const char *operation) {
    if (operation == nullptr) return;
    GLint errorCode = glGetError();
    int count = 0;
    while (errorCode) {
        ALOGE("Operation %s() error 0x%x", operation, errorCode);
        // 超过特定次数，停止检查错误，避免循环过大
        if (count > MAX_ERROR) {
            ALOGE("reach max error, broken");
            break;
        }
        count++;
    }
}