//
// Created by Joe.Lee on 2022/1/12.
//
#define LOG_TAG "CustomShape"

#include "CustomShape.h"
#include "common/Common.h"
#include "third_party/stb/stb_image.h"
#include "common/ShaderUtils.h"

const std::string CustomShape::VERTEX_PATH = DIR_DATA + "/files/custom_vertex_code.glsl";
const std::string CustomShape::FRAGMENT_PATH = DIR_DATA + "/files/custom_fragment_code.glsl";
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
    if (m_pShader == nullptr) {
        ALOGE("create shader failed, maybe no memory");
        return -2;
    }
    ALOGD("init success, shader %p", m_pShader);
    initOpenGL();
    m_pShader->use();
    return 0;
}

int CustomShape::initOpenGL() {
    const float vertices[] = {
            //     坐标                颜色          纹理坐标
            -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 左下
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // 右下
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, // 右上
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f // 左上
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
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float), (void *) 0);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float),
                          (void *) (3 * sizeof(float)));
    glEnableVertexAttribArray(1);
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE,
                          8 * sizeof(float), (void *) (6 * sizeof(float)));
    glEnableVertexAttribArray(2);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

    // 创建纹理
    glGenBuffers(2, m_texture);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_texture[0]);
    // 设置纹理环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // 设置纹理填充方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    // 载入图像数据，并填充到纹理
    ShaderUtils::LoadImageToTexture(IMAGE_FILE_PATH);
    // 为当前纹理自动生成多级渐远纹理
    glGenerateMipmap(GL_TEXTURE_2D);

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, m_texture[1]);
    // 设置纹理环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // 设置纹理填充方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    // 载入图像数据，并填充到纹理
    ShaderUtils::LoadImageToTexture(IMAGE_FILE_PATH1);
    // 为当前纹理自动生成多级渐远纹理
    glGenerateMipmap(GL_TEXTURE_2D);

    m_pShader->use();
    m_pShader->setInt("texture0", 0);
    m_pShader->setInt("texture1", 1);
    return 0;
}

void CustomShape::draw() {
    if (DEBUG) {
        ALOGD("E");
    }
    m_pShader->use();
    glBindTexture(GL_TEXTURE_2D, m_texture[0]);
    glBindTexture(GL_TEXTURE_2D, m_texture[1]);
    glBindVertexArray(m_VAO);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
}