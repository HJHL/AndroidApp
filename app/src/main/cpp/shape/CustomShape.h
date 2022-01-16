//
// Created by Joe.Lee on 2022/1/12.
//

#ifndef ANDROIDAPP_CUSTOMSHAPE_H
#define ANDROIDAPP_CUSTOMSHAPE_H

#include "IShape.h"
#include "common/Shader.h"

class CustomShape : public IShape {
public:
    CustomShape();

    ~CustomShape() noexcept;

    void draw() override;

    void setRenderScreenSize(const int width, const int height) override;

private:
    static constexpr bool DEBUG = false;
    static const std::string VERTEX_PATH;
    static const std::string SCREEN_VERTEX_PATH;
    static const std::string FRAGMENT_PATH;
    static const std::string SCREEN_FRAGMENT_PATH;
    static const std::string IMAGE_FILE_PATH;
    static const std::string IMAGE_FILE_PATH1;
    int m_renderScreenWidth;
    int m_renderScreenHeight;
    Shader *m_pShader;
    Shader *m_pScreenShader;
    unsigned int m_texture;
    unsigned int m_VAO = 0;
    unsigned int m_FBO = 0;
    unsigned int m_texture1;

    int init();

    int initOpenGL();

    unsigned int createFramebuffer();

    inline static void checkFramebuffer() {
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
            ALOGD("frame buffer complete!");
        } else {
            ALOGE("frame buffer is not completed! error code %d", glGetError());
        }
    }
};


#endif //ANDROIDAPP_CUSTOMSHAPE_H
