//
// Created by Joe.Lee on 2022/1/8.
//
#define LOG_TAG "NativeRender"

#include "NativeRender.h"
#include "common/Log.h"
#include "shape/Triangle.h"
#include <GLES3/gl3.h>
#include "glm/glm.hpp"
#include "shape/CustomShape.h"

NativeRender *NativeRender::CreateInstance() {
    NativeRender *nativeRender = new NativeRender();
    if (nativeRender == nullptr) {
        ALOGE("create native render failed");
        return nullptr;
    }
    return nativeRender;
}

void NativeRender::DestroyInstance() {
}

void NativeRender::OnDrawFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
    m_shape->draw();
}

void NativeRender::OnSurfaceCreated() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
}

void NativeRender::OnSurfaceChanged(int width, int height) {
    glViewport(0, 0, width, height);
}

void NativeRender::SetImageData(int format, int width, int height, const char *bytes) {

}

NativeRender::NativeRender() {
    m_shape = new Triangle();
}

NativeRender::~NativeRender() {
    delete m_shape;
    m_shape = nullptr;
}