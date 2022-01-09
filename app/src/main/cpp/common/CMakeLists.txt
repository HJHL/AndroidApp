add_library(common SHARED
        JNIUtils.cpp
        Log.cpp
        ShaderUtils.cpp)

target_link_libraries(common PUBLIC
        android
        GLESv3
        log
        )