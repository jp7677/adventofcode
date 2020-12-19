#!/bin/bash
set -e

g++ -std=c++17 -Werror inc/catch_amalgamated.cpp src/main.cpp -c
g++ -std=c++17 -Werror \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode
