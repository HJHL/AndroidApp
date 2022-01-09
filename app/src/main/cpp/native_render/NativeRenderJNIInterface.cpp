//
// Created by Joe.Lee on 2022/1/8.
//
#include "NativeRender.h"
#include "jni.h"
#include "common/Log.h"
#include "common/JNIUtils.h"

#define LOG_TAG "NativeRenderJNIInterface"
#define NATIVE_RENDER_CLASS_NAME "me/lijiahui/androidapp/widget/MyNativeRender"

static NativeRender *g_nativeRender = nullptr;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL native_OnInit(JNIEnv *env, jobject clazz) {
    ALOGI("on native init");
    g_nativeRender = NativeRender::CreateInstance();
    if (g_nativeRender == nullptr) {
        ALOGE("create native render failed");
    }
}

JNIEXPORT void JNICALL native_OnUninit(JNIEnv *env, jobject clazz) {
    ALOGI("on native uninit");
    if (g_nativeRender == nullptr) {
        ALOGE("uninit failed, handle is null");
        return;
    } else {
        g_nativeRender->DestroyInstance();
        g_nativeRender = nullptr;
    }
}

JNIEXPORT void JNICALL native_OnSurfaceCreated(JNIEnv *env, jobject clazz) {
    if (g_nativeRender == nullptr) {
        ALOGE("onSurfaceCreated failed, can not get native render");
        return;
    }
    g_nativeRender->OnSurfaceCreated();
}

JNIEXPORT void JNICALL native_OnSurfaceChanged(JNIEnv *env, jobject clazz,
                                               jint width, jint height) {
    ALOGI("%dx%d", width, height);
    if (g_nativeRender == nullptr) {
        ALOGE("onSurfaceChanged failed, can not get native render");
        return;
    }
    g_nativeRender->OnSurfaceChanged(width, height);
}

JNIEXPORT void JNICALL native_OnDrawFrame(JNIEnv
                                          *env,
                                          jobject clazz) {
    if (g_nativeRender == nullptr) {
        //ALOGE("onDrawFrame failed, can not get native render");
        return;
    }
    g_nativeRender->OnDrawFrame();
}

JNIEXPORT void JNICALL native_setImageData(JNIEnv *env, jobject clazz,
                                           jint format, jint width, jint height, jbyteArray bytes) {
    const int len = env->GetArrayLength(bytes);
    char *buf = new char[len];
    env->GetByteArrayRegion(bytes, 0, len, (jbyte *) (buf));
    g_nativeRender->SetImageData(format, width, height, buf);
    delete[] buf;
    env->DeleteLocalRef(bytes);
}

static JNINativeMethod g_nativeRenderMethods[] = {
        {"native_OnInit",           "()V",      (void *) (native_OnInit)},
        {"native_OnUninit",         "()V",      (void *) (native_OnUninit)},
        {"native_OnSurfaceCreated", "()V",      (void *) (native_OnSurfaceCreated)},
        {"native_OnSurfaceChanged", "(II)V",    (void *) (native_OnSurfaceChanged)},
        {"native_OnDrawFrame",      "()V",      (void *) (native_OnDrawFrame)},
        {"native_setImageData",     "(III[B)V", (void *) (native_setImageData)},
};

jint JNI_OnLoad(JavaVM *jvm, void *p) {
    JNIEnv *env = nullptr;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        ALOGE("could not get env from jvm");
        return JNI_ERR;
    }
    jint ret = JNIUtils::RegisterNativeMethods(env, NATIVE_RENDER_CLASS_NAME, g_nativeRenderMethods,
                                               sizeof(g_nativeRenderMethods) /
                                               sizeof(g_nativeRenderMethods[0]));
    if (ret != JNI_OK) {
        ALOGE("register native methods failed");
        return ret;
    }
    ALOGI("register native methods success!");
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *jvm, void *p) {
    JNIEnv *env = nullptr;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        ALOGE("could not get env from jvm");
        return;
    }
    ALOGI("unregister native method");
    JNIUtils::UnregisterNativeMethods(env, NATIVE_RENDER_CLASS_NAME);
}

#ifdef __cplusplus
};
#endif
