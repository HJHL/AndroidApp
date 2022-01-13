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

private:
    static constexpr bool DEBUG = false;
    static const std::string VERTEX_PATH;
    static const std::string FRAGMENT_PATH;
    static const std::string IMAGE_FILE_PATH;
    static const std::string IMAGE_FILE_PATH1;
    Shader *m_pShader;
    unsigned int m_texture[2] = {0};
    unsigned int m_VAO = 0;

    int init();

    int initOpenGL();
};


#endif //ANDROIDAPP_CUSTOMSHAPE_H
