//
// Created by Joe.Lee on 2022/1/8.
//

#ifndef ANDROIDAPP_JNIUTILS_H
#define ANDROIDAPP_JNIUTILS_H

#include "jni.h"

class JNIUtils {
public:
    /**
     * Register some native methods to Java class
     *
     * @param env           the Java environment
     * @param className     target class which has native methods to be implemented
     * @param methods       the methods that want to register for [className]
     * @param numOfMethods  number of methods that want to register
     * */
    static int
    RegisterNativeMethods(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                          size_t numOfMethods);

    /**
     * Unregister native methods which had registered to a Java class
     *
     * @param env           the Java environment
     * @param className     target class which want to unload native methods
     * */
    static void UnregisterNativeMethods(JNIEnv *env, const char *className);
};


#endif //ANDROIDAPP_JNIUTILS_H
