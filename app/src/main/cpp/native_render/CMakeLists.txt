add_library(native_render SHARED
        NativeRender.cpp
        NativeRenderJNIInterface.cpp
        )

find_library(log-lib log)

target_include_directories(native_render PRIVATE
        ${CMAKE_SOURCE_DIR}
        ${CMAKE_SOURCE_DIR}/third_party
        )
target_link_libraries(native_render PRIVATE
        common
        shape
        ${log-lib}
        GLESv3
        android
        )