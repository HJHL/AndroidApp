add_library(shape SHARED
        Triangle.cpp
        IShape.cpp
        CustomShape.cpp
        )

target_include_directories(shape PRIVATE
        ${CMAKE_SOURCE_DIR}
        )

target_link_libraries(shape PRIVATE
        common
        )