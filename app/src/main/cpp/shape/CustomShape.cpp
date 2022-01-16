//
// Created by Joe.Lee on 2022/1/12.
//
#define LOG_TAG "CustomShape"

#include "CustomShape.h"
#include "common/Common.h"
#include "third_party/stb/stb_image.h"
#include "common/ShaderUtils.h"
#include <chrono>
#include <cinttypes>

const std::string CustomShape::VERTEX_PATH = DIR_DATA + "/files/custom_vertex_code.glsl";
const std::string CustomShape::SCREEN_VERTEX_PATH = DIR_DATA + "/files/screen_vertex_shader.glsl";
const std::string CustomShape::FRAGMENT_PATH = DIR_DATA + "/files/custom_fragment_code.glsl";
const std::string CustomShape::SCREEN_FRAGMENT_PATH =
        DIR_DATA + "/files/screen_fragment_shader.glsl";
const std::string CustomShape::IMAGE_FILE_PATH = DIR_DATA + "/files/wall.jpeg";
const std::string CustomShape::IMAGE_FILE_PATH1 = DIR_DATA + "/files/awesomeface.png";

CustomShape::CustomShape() {
    int ret = 0;
    ret = init();
    if (ret != 0) {
        ALOGE("init failed, ret %d", ret);
    }
}

CustomShape::~CustomShape() noexcept {
    if (m_pShader != nullptr) {
        delete m_pShader;
        m_pShader = nullptr;
    }
}

int CustomShape::init() {
    m_pShader = new Shader(VERTEX_PATH, FRAGMENT_PATH);
    m_pScreenShader = new Shader(SCREEN_VERTEX_PATH, SCREEN_FRAGMENT_PATH);
    if (m_pShader == nullptr) {
        ALOGE("create shader failed, maybe no memory");
        return -2;
    }
    if (m_pScreenShader == nullptr) {
        ALOGE("create screen shader failed");
        return -2;
    }
    ALOGD("init success, shader %p, screen shader %p", m_pShader, m_pScreenShader);
    initOpenGL();
    return 0;
}

int CustomShape::initOpenGL() {
    const float vertices[] = {
            //     坐标                颜色          纹理坐标
            -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 左下
            1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // 右下
            1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, // 右上
            -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f // 左上
    };
    const unsigned int indices[] = {
            0, 1, 2,
            0, 2, 3,
    };

    unsigned int VBO = 0, EBO = 0;
    // 创建一个 VBO
    glGenBuffers(1, &VBO);
    // 创建一个 m_VAO
    glGenVertexArrays(1, &m_VAO);
    // 创建一个 EBO
    glGenBuffers(1, &EBO);
    // 绑定 m_VAO
    glBindVertexArray(m_VAO);
    // 绑定该 VBO 的类型，并将顶点数据从内存拷贝到显存（VBO）
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
    // 绑定该 EBO 的类型，并将顶点数据从内存拷贝到显存（EBO）
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);
    // 设置顶点属性指针，并启用该属性
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float),
                          (void *) 0);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float),
                          (void *) (3 * sizeof(float)));
    glEnableVertexAttribArray(1);
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE,
                          8 * sizeof(float), (void *) (6 * sizeof(float)));
    glEnableVertexAttribArray(2);

    // setInt 前一定要调用 glUseProgram！！！
    m_pShader->use();
    // 告诉 texture0 变量绑定的是 texture0
    m_pShader->setInt("texture0", 0);

    m_pScreenShader->use();
    m_pScreenShader->setInt("screenTexture", 0);

    m_texture1 = ShaderUtils::LoadImageToTexture2D(IMAGE_FILE_PATH);
    return 0;
}

void CustomShape::draw() {
    if (DEBUG) {
        ALOGD("E");
    }
    // 离屏渲染
    glBindFramebuffer(GL_FRAMEBUFFER, m_FBO);
    glClearColor(0.1, 0.2, 0.3, 1.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    m_pShader->use();
    glBindVertexArray(m_VAO);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_texture1);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);

    // 显示
    glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE);
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
    m_pScreenShader->use();
    glBindVertexArray(m_VAO);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_texture);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);
}

void CustomShape::setRenderScreenSize(const int width, const int height) {
    ALOGD("render screen dimension %dx%d", width, height);
    m_renderScreenWidth = width;
    m_renderScreenHeight = height;
    if (m_FBO != ShaderUtils::INVALID_HANDLE) {
        glDeleteFramebuffers(1, &m_FBO);
        m_FBO = ShaderUtils::INVALID_HANDLE;
    }
    m_FBO = createFramebuffer();
}

unsigned int CustomShape::createFramebuffer() {
    unsigned int frameBuffer = ShaderUtils::INVALID_HANDLE;
    glGenFramebuffers(1, &frameBuffer);
    if (frameBuffer == ShaderUtils::INVALID_HANDLE) {
        ALOGE("create frame buffer failed");
        return frameBuffer;
    }
    glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
    glGenTextures(1, &m_texture);
    ALOGD("create texture %u", m_texture);
    glBindTexture(GL_TEXTURE_2D, m_texture);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, m_renderScreenWidth, m_renderScreenHeight,
                 0, GL_RGB, GL_UNSIGNED_BYTE, nullptr);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_texture, 0);
    checkFramebuffer();
    // 绑定回默认的 FrameBuffer
    glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE);
    return frameBuffer;
}