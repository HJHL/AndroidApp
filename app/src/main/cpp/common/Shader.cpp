//
// Created by Joe.Lee on 2022/1/12.
//

#define LOG_TAG "Shader"

#include "Shader.h"
#include "ShaderUtils.h"
#include <sstream>
#include <fstream>

Shader::Shader(const std::string &vertexShaderFile, const std::string &fragmentShaderFile) {
    std::ifstream vertexFile(vertexShaderFile);
    std::ifstream fragmentFile(fragmentShaderFile);
    std::stringstream vertexStream;
    std::stringstream fragmentStream;
    vertexStream << vertexFile.rdbuf();
    fragmentStream << fragmentFile.rdbuf();
    int ret = init(vertexStream.str().c_str(), fragmentStream.str().c_str());
    if (ret != 0) {
        ALOGE("init fail, ret %d", ret);
    }
    vertexFile.close();
    fragmentFile.close();
}

int Shader::init(const char *const vertexShaderCode, const char *const fragmentShaderCode) {
    if (vertexShaderCode == nullptr || fragmentShaderCode == nullptr) {
        ALOGE("invalid argument: vertex %p fragment %p", vertexShaderCode, fragmentShaderCode);
        return -1;
    }
    m_program = ShaderUtils::CreateProgram(vertexShaderCode, fragmentShaderCode);
    if (DEBUG) {
        ALOGD("program handle %u", m_program);
    }
    if (m_program == INVALID_HANDLE) {
        ALOGE("invalid program handle, create program failed");
        return -2;
    }
    return 0;
}
