//
// Created by Joe.Lee on 2022/1/12.
//
#define LOG_TAG "CustomShape"

#include "CustomShape.h"
#include "common/Common.h"

const std::string CustomShape::VERTEX_PATH = DIR_DATA + "/files/custom_vertex_code.glsl";
const std::string CustomShape::FRAGMENT_PATH = DIR_DATA + "/files/custom_fragment_code.glsl";

CustomShape::CustomShape() {
    int ret = 0;
    ret = init();
    if (ret != 0) {
        ALOGE("init failed, ret %d", ret);
    }
}

CustomShape::~CustomShape() noexcept {
    if (m_shader != nullptr) {
        delete m_shader;
        m_shader = nullptr;
    }
}

int CustomShape::init() {
    m_shader = new Shader(VERTEX_PATH, FRAGMENT_PATH);
    if (m_shader == nullptr) {
        ALOGE("create shader failed, maybe no memory");
        return -2;
    }
    ALOGD("init success, shader %p", m_shader);
    return 0;
}

void CustomShape::draw() {
}