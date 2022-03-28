add_library(jni_ffmpeg SHARED
        FFMpegJNI.cpp)

target_include_directories(jni_ffmpeg PRIVATE
        ${CMAKE_SOURCE_DIR}/third_party/ffmpeg
        ${CMAKE_SOURCE_DIR}
        )

include(${CMAKE_SOURCE_DIR}/third_party/ffmpeg/CMakeLists.txt)
target_link_libraries(jni_ffmpeg PRIVATE
        common
        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        )

add_library(access_hide_api SHARED
        AccessHiddenApi.cpp
        )
target_include_directories(access_hide_api PRIVATE
        ${CMAKE_SOURCE_DIR}
        )
target_link_libraries(access_hide_api PRIVATE
        common
        )