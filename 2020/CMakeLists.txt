cmake_minimum_required(VERSION 3.17)
project(adventofcode)

set(CMAKE_CXX_STANDARD 17)
add_compile_options(-Wall -Wextra -Werror)

FILE(GLOB days src/day*.cpp) # This might be a bad idea
add_executable(${CMAKE_PROJECT_NAME}
    src/main.cpp
    ${days})

target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE
    pthread)
