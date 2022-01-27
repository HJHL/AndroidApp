#!/bin/bash

# filename: compile_ffmpeg.sh

set -e

# 安卓 API 版本
API=21
OS=android
PREFIX="${pwd}/out/"
ARCH=arm64
CPU=armv8-a
CFLAGS="-Os"

ANDROID_HOME=$HOME/.local/android
# NDK 版本
NDK=$ANDROID_HOME/ndk/21.4.7075529
TOOLCHAINS=$NDK/toolchains/llvm/prebuilt/linux-x86_64
CC=$TOOLCHAINS/bin/aarch64-linux-android$API-clang
CXX=$TOOLCHAINS/bin/aarch64-linux-android$API-clang++
SYSROOT=$TOOLCHAINS/sysroot
CROSS_PREFIX=$TOOLCHAINS/bin/aarch64-linux-android-
NM=$TOOLCHAINS/bin/llvm-nm
STRIP=$TOOLCHAINS/bin/llvm-strip
PKG_CFG=$TOOLCHAINS/bin/llvm-config

function build_ffmpeg
{
echo "Start build ffmpeg...for $CPU"
SECONDS=0
./configure \
    --prefix=$PREFIX    \
    --disable-static    \
    --enable-shared     \
    --arch=$ARCH        \
    --cpu=$CPU          \
    --target-os=$OS     \
    --cc=$CC            \
    --cxx=$CXX          \
    --enable-cross-compile  \
    --cross-prefix=$CROSS_PREFIX    \
    --sysroot=$SYSROOT      \
    --nm=$NM                \
    --strip=$STRIP          \
    --pkg-config=$PKG_CFG   \
    --enable-jni            \
    --enable-mediacodec     \
    --enable-pic            \
    --enable-hwaccels       \
    --disable-doc           \
    --extra-cflags=$CFLAGS  \
    --extra-cxxflags=$CXXFLAGS  \

make -j
make install
duration=$SECONDS
echo "Compile for $CPU success! cost time $(($duration / 60)) mins $(($duration % 60)) seconds"
}

# 编译 Arm 64 位
ARCH=arm64
CPU=armv8-a
PREFIX=$(pwd)/out/$OS/$CPU
build_ffmpeg
make clean

# 编译 Arm 32 位
ARCH=arm
CPU=armv7-a
CC=$TOOLCHAINS/bin/armv7a-linux-androideabi$API-clang
CXX=$TOOLCHAINS/bin/armv7a-linux-androideabi$API-clang++
CROSS_PREFIX=$TOOLCHAINS/bin/armv-linux-androideabi-
PREFIX=$(pwd)/out/$OS/$CPU
build_ffmpeg
make clean
