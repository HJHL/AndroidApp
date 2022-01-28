#define LOG_TAG "FFMpegJNI"

#include <string>
#include "common/JNIUtils.h"
#include "common/Log.h"

#define NATIVE_FFMPEG_CLASS_NAME "me/lijiahui/androidapp/demo/ffmpeg/FfmpegFragment"

#ifdef __cplusplus
extern "C" {
#endif
#include "libavcodec/version.h"
#include "libavdevice/version.h"
#include "libavfilter/version.h"
#include "libavformat/version.h"
#include "libavutil/version.h"
#include "libswresample/version.h"
#include "libswscale/version.h"
#include "libavcodec/avcodec.h"
#include <jni.h>

JNIEXPORT jstring JNICALL native_getInfo(JNIEnv *env, jclass clazz) {
    std::string result("FFMpeg info: \n");
    result.append("avcodec version: ");
    result.append(AV_STRINGIFY(LIBAVCODEC_VERSION));
    result.append("\n");

    result.append("avdevice version: ");
    result.append(AV_STRINGIFY(LIBAVDEVICE_VERSION));
    result.append("\n");

    result.append("avfilter version: ");
    result.append(AV_STRINGIFY(LIBAVFILTER_VERSION));
    result.append("\n");

    result.append("avformat version: ");
    result.append(AV_STRINGIFY(LIBAVFORMAT_VERSION));
    result.append("\n");

    result.append("avutil version: ");
    result.append(AV_STRINGIFY(LIBAVUTIL_VERSION));
    result.append("\n");

    result.append("swresample version: ");
    result.append(AV_STRINGIFY(LIBSWRESAMPLE_VERSION));
    result.append("\n");

    result.append("swscale version: ");
    result.append(AV_STRINGIFY(LIBSWSCALE_VERSION));
    result.append("\n");

    result.append("build configure: ");
    result.append(avcodec_configuration());
    result.append("\n");

    return env->NewStringUTF(result.c_str());
}

static JNINativeMethod g_ffmpegNativeMethods[] = {
        {"native_getInfo", "()Ljava/lang/String;", (void *) native_getInfo},
};

jint JNI_OnLoad(JavaVM *jvm, void *p) {
    JNIEnv *env = nullptr;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        ALOGE("could not get env from jvm");
        return JNI_ERR;
    }
    jint ret = JNIUtils::RegisterNativeMethods(env, NATIVE_FFMPEG_CLASS_NAME, g_ffmpegNativeMethods,
                                               sizeof(g_ffmpegNativeMethods) /
                                               sizeof(g_ffmpegNativeMethods[0]));
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
    JNIUtils::UnregisterNativeMethods(env, NATIVE_FFMPEG_CLASS_NAME);
}

#ifdef __cplusplus
};
#endif