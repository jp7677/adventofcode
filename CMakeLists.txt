cmake_minimum_required(VERSION 3.17)
project(adventofcode)

set(CMAKE_CXX_STANDARD 17)
add_compile_options(-Wall -Wextra -Werror)

include_directories(inc)
include_directories(src)

FILE(GLOB days src/day*.cpp) # This might be bad idea

add_executable(adventofcode
    inc/catch_amalgamated.cpp
    inc/catch_amalgamated.hpp
    src/main.cpp
    src/util.h
    src/days_private.h
    ${days})

target_link_libraries(adventofcode PRIVATE
    pthread)
