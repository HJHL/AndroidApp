#define LOG_TAG "AccessHiddenApi"

#include <jni.h>
#include "common/Log.h"

#define CLASS_NAME_VM_RUNTIME "dalvik/system/VMRuntime"
#define CLASS_NAME_ZYGOTE_INIT "com/android/internal/os/ZygoteInit"
#define CLASS_NAME_STRING "java/lang/String"

// reference: https://bbs.pediy.com/thread-268936.htm
bool setHiddenApiExemptions(JNIEnv *const env) {
    if (env == nullptr) {
        ALOGW("invalid arg, JNIEnv is null");
        return false;
    }
    jclass zygoteInitClass = env->FindClass(CLASS_NAME_ZYGOTE_INIT);
    if (zygoteInitClass == nullptr) {
        ALOGE("could not find ZygoteInit");
        env->ExceptionClear();
        return false;
    }

    jmethodID setApiDenylistExemptions = env->GetStaticMethodID(zygoteInitClass, "setApiDenylistExemptions",
                                                          "([Ljava/lang/String;)V");
    if (setApiDenylistExemptions == nullptr) {
        ALOGW("could not find method: setApiDenylistExemptions");
        env->ExceptionClear();
        return false;
    }

    jclass stringClass = env->FindClass(CLASS_NAME_STRING);
    jstring allMethodSignature = env->NewStringUTF("L");
    jobjectArray objectArray = env->NewObjectArray(1, stringClass, nullptr);
    env->SetObjectArrayElement(objectArray, 0, allMethodSignature);
    // set hidden api exemptions
    env->CallStaticVoidMethod(zygoteInitClass, setApiDenylistExemptions, objectArray);
    // clear up
    env->DeleteLocalRef(allMethodSignature);
    env->DeleteLocalRef(objectArray);

    ALOGD("enable hidden api access success!");
    return true;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    ALOGI("E");
    JNIEnv *env = nullptr;
    jint ret = JNI_OK;
    if ((ret = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6)) != JNI_OK) {
        ALOGE("find java env failed: %d", ret);
        return JNI_VERSION_1_6;
    }
    if (!setHiddenApiExemptions(env)) {
        ALOGW("enable hidden api access failed");
    } else {
        ALOGI("enable hidden api access success");
    }
    ALOGI("X");
    return JNI_VERSION_1_6;
}
