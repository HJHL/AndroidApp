//
// Created by Joe.Lee on 2022/1/8.
//

#include "JNIUtils.h"

int JNIUtils::RegisterNativeMethods(JNIEnv *env, const char *className,
                                    const JNINativeMethod *methods, size_t numOfMethods) {
    if (env == nullptr || className == nullptr) {
        return JNI_EINVAL;
    }
    if (methods == nullptr || numOfMethods == 0) {
        return JNI_EINVAL;
    }
    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        return JNI_ERR;
    }
    jint ret = env->RegisterNatives(clazz, methods, numOfMethods);
    if (ret != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_OK;
}

void JNIUtils::UnregisterNativeMethods(JNIEnv *env, const char *className) {
    if (env == nullptr || className == nullptr) {
        return;
    }
    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        return;
    }
    env->UnregisterNatives(clazz);
}
