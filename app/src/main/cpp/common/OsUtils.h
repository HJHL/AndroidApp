//
// Created by bytedance on 2022/1/13.
//

#ifndef ANDROIDAPP_OSUTILS_H
#define ANDROIDAPP_OSUTILS_H


#include <string>
#include <sys/stat.h>

class OsUtils {
public:
    inline static bool isFileExist(const std::string &fileName) {
        struct stat buf;
        return (stat(fileName.c_str(), &buf) == 0);
    }
};


#endif //ANDROIDAPP_OSUTILS_H
