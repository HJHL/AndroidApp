add_library(jni SHARED
        FFMpegJNI.cpp)

target_include_directories(jni PRIVATE
        ${CMAKE_SOURCE_DIR}/third_party/ffmpeg)

include(${CMAKE_SOURCE_DIR}/third_party/ffmpeg/CMakeLists.txt)
target_link_libraries(jni PRIVATE
        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        )