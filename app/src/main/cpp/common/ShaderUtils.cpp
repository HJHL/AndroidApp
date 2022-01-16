//
// Created by Joe.Lee on 2022/1/8.
//
#define LOG_TAG "ShaderUtils"

#include "ShaderUtils.h"
#include "OsUtils.h"
#include "third_party/stb/stb_image.h"
#include <malloc.h>

// 异常时（如图像没有绘制），取消掉下面注释的开关
// 线上环境禁止打开，因为 OpenGL 异常查询会让 CPU 与 GPU 间进行通信，频繁地进行会严重损耗性能
#define DEBUG

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
    std::string s;
    if (type == GL_VERTEX_SHADER) {
        s = "Compile vertex shader";
    } else if (type == GL_FRAGMENT_SHADER) {
        s = "Compile fragment shader";
    } else {
        s = "Compile shader";
    }
    ALOGD("[%s] check source %s", s.c_str(), source);
    checkShaderInfo(shaderHandle, GL_COMPILE_STATUS, s);
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
    checkProgramInfo(programHandle, GL_LINK_STATUS, "Link Program");
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

void ShaderUtils::checkShaderInfo(const unsigned int shader, const int type,
                                  const std::string &operation) {
    checkInfo(shader, type, operation, Shader);
}

void ShaderUtils::checkProgramInfo(const unsigned int program, const int type,
                                   const std::string &operation) {
    checkInfo(program, type, operation, Program);
}

void ShaderUtils::checkInfo(const unsigned int handle, const int type,
                            const std::string &operation, CheckType checkType) {
    GLint status = GL_TRUE;
    switch (checkType) {
        case Program:
            glGetProgramiv(handle, type, &status);
            break;
        case Shader:
            glGetShaderiv(handle, type, &status);
            break;
        default:
            break;
    }
    if (status == GL_TRUE) {
        ALOGD("[%s] success, no need check log", operation.c_str());
        return;
    }
    GLint infoLength = 0;
    switch (checkType) {
        case Program:
            glGetProgramiv(handle, GL_INFO_LOG_LENGTH, &infoLength);
            break;
        case Shader:
            glGetShaderiv(handle, GL_INFO_LOG_LENGTH, &infoLength);
            break;
        default:
            break;
    }
    if (infoLength == 0) {
        ALOGE("get info log length 0");
        return;
    }
    char *infoBuffer = (char *) malloc(infoLength);
    if (infoBuffer == nullptr) {
        ALOGE("malloc info log buffer failed");
        return;
    }
    switch (checkType) {
        case Program:
            glGetProgramInfoLog(handle, infoLength, nullptr, infoBuffer);
            break;
        case Shader:
            glGetShaderInfoLog(handle, infoLength, nullptr, infoBuffer);
            break;
        default:
            break;
    }
    ALOGE("[%s] failed, msg %s", operation.c_str(), infoBuffer);
    free(infoBuffer);
    infoBuffer = nullptr;
}

unsigned int ShaderUtils::LoadImageToTexture2D(const std::string &imagePath) {
    if (!OsUtils::isFileExist(imagePath)) {
        ALOGE("image file can not access, %s", imagePath.c_str());
        return INVALID_HANDLE;
    }
    unsigned int textureId;
    glGenTextures(1, &textureId);
    int width = 0, height = 0, channels = 0;
    unsigned char *imageData = nullptr;
//     载入图片前，绕 Y 轴翻转
//    stbi_set_flip_vertically_on_load(true);
    imageData = stbi_load(imagePath.c_str(), &width, &height, &channels, 0);
    ALOGD("image %dx%d channel %d address %p", width, height, channels, imageData);
    if (imageData == nullptr) {
        ALOGE("load image failed");
        glDeleteTextures(1, &textureId);
        return INVALID_HANDLE;
    }
    GLenum format;
    switch (channels) {
        case 1:
            format = GL_RED;
            break;
        case 3:
            format = GL_RGB;
            break;
        case 4:
            format = GL_RGBA;
            break;
        default:
            format = GL_RGB;
            break;
    }
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, imageData);
    glGenerateMipmap(GL_TEXTURE_2D);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    stbi_image_free(imageData);
    imageData = nullptr;
    return textureId;
}