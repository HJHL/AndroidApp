add_library(common SHARED
        JNIUtils.cpp
        Log.cpp
        ShaderUtils.cpp
        Shader.cpp
        std_image.cpp
        )

target_link_libraries(common PUBLIC
        android
        GLESv3
        log
        )

target_include_directories(common PUBLIC
        ${CMAKE_SOURCE_DIR}
        )