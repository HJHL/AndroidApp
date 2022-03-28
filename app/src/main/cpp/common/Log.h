//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_LOG_H
#define ANDROIDAPP_LOG_H

#include <android/log.h>

#ifndef LOG_TAG
#define LOG_TAG ""
#endif

#define ALOGV(fmt, ...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, \
    "%s %d %s() " fmt, __FILE_NAME__, __LINE__, __FUNCTION__, ##__VA_ARGS__)

#define ALOGD(fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, \
    "%s %d %s() " fmt, __FILE_NAME__, __LINE__, __FUNCTION__, ##__VA_ARGS__)

#define ALOGI(fmt, ...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, \
    "%s %d %s() " fmt, __FILE_NAME__, __LINE__, __FUNCTION__, ##__VA_ARGS__)

#define ALOGW(fmt, ...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, \
    "%s %d %s() " fmt, __FILE_NAME__, __LINE__, __FUNCTION__, ##__VA_ARGS__)

#define ALOGE(fmt, ...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, \
    "%s %d %s() " fmt, __FILE_NAME__, __LINE__, __FUNCTION__, ##__VA_ARGS__)

#endif //ANDROIDAPP_LOG_H
