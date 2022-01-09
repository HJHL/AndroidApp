//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_TRIANGLE_H
#define ANDROIDAPP_TRIANGLE_H

#include "IShape.h"
#include <GLES3/gl3.h>

class Triangle : public IShape {
public:
    Triangle();

    virtual ~Triangle() noexcept;

    void draw() override;

private:
    unsigned int m_programHandle;
    static const char *const VERTEX_SHADER_CODE;
    static const char *const FRAGMENT_SHADER_CODE;

    constexpr static GLfloat VERTICES[] = {
            0.0f, 0.5f, 0.0f,
            -0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f,
    };

    int init();
};


#endif //ANDROIDAPP_TRIANGLE_H
