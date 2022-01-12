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
    static const std::string VERTEX_PATH;
    static const std::string FRAGMENT_PATH;
    Shader *m_shader;

    int init();
};


#endif //ANDROIDAPP_CUSTOMSHAPE_H
