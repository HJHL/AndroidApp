
#include <string>

#ifdef __cplusplus
extern "C" {
#endif
#include "libavcodec/version.h"
#include "libavcodec/avcodec.h"
#include <jni.h>

#ifdef __cplusplus
};
#endif

extern "C"
JNIEXPORT jstring JNICALL
Java_me_lijiahui_androidapp_demo_FFMpegActivity_native_1encodec_1info(JNIEnv *env, jobject thiz) {
    void *i = 0;
    const AVCodec *avCodec = nullptr;
    std::string res("Encodec info: \n");
    while (avCodec = av_codec_iterate(&i)) {
        if (av_codec_is_encoder(avCodec))
            res.append(avCodec->name);
        res.append("\n");
    }
    return env->NewStringUTF(res.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_me_lijiahui_androidapp_demo_FFMpegActivity_native_1decodec_1info(JNIEnv *env, jobject thiz) {
    void *i = 0;
    const AVCodec *avCodec = nullptr;
    std::string res("Decodec info: \n");
    while (avCodec = av_codec_iterate(&i)) {
        if (av_codec_is_decoder(avCodec))
            res.append(avCodec->name);
        res.append("\n");
    }
    return env->NewStringUTF(res.c_str());
}