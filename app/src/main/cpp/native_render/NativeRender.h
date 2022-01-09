//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_NATIVERENDER_H
#define ANDROIDAPP_NATIVERENDER_H

#include "shape/IShape.h"

class NativeRender {
public:
    /**
     * 创建一个新的 native render 实例
     * */
    static NativeRender *CreateInstance();

    /**
     * 请在上层 Surface 创建时调用
     * */
    void OnSurfaceCreated();

    /**
     * 请在上层 Surface 发生变化时调用
     *
     * @param width     Surface 的宽
     * @param height    Surface 的高
     * */
    void OnSurfaceChanged(int width, int height);

    /**
     * 请在每帧绘制时调用
     * */
    void OnDrawFrame();

    void SetImageData(int format, int width, int height, const char *bytes);

    void DestroyInstance();

private:
    IShape *m_shape;

    NativeRender();

    ~NativeRender();
};


#endif //ANDROIDAPP_NATIVERENDER_H
