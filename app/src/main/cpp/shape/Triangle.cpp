//
// Created by Joe.Lee on 2022/1/8.
//

#define LOG_TAG "Triangle"

#include "Triangle.h"
#include "common/ShaderUtils.h"
#include "common/Log.h"

const char *const ::Triangle::VERTEX_SHADER_CODE =
        "#version 300 es                            \n"
        "layout(location = 0) in vec4 vPosition;    \n"
        "void main() {                              \n"
        "  gl_Position = vPosition;                 \n"
        "}";
const char *const ::Triangle::FRAGMENT_SHADER_CODE =
        "#version 300 es                            \n"
        "precision mediump float;                   \n"
        "out vec4 fragColor;                        \n"
        "void main() {                              \n"
        "  fragColor = vec4(1.0, 0.0, 0.0, 1.0);    \n"
        "}";;

Triangle::Triangle() {
    init();
}

Triangle::~Triangle() noexcept {

}

void Triangle::draw() {
    if (m_programHandle == ShaderUtils::INVALID_HANDLE) {
        ALOGE("invalid program handle");
        return;
    }
    glUseProgram(m_programHandle);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, VERTICES);
    glEnableVertexAttribArray(0);
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

int Triangle::init() {
    m_programHandle = ShaderUtils::CreateProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
    ALOGI("program handle %d", m_programHandle);
    return 0;
}