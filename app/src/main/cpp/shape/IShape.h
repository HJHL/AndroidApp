//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_ISHAPE_H
#define ANDROIDAPP_ISHAPE_H

class IShape {
public:
    virtual ~IShape();

    virtual void draw() = 0;

    virtual void setRenderScreenSize(const int width, const int height) = 0;
};

#endif //ANDROIDAPP_ISHAPE_H
