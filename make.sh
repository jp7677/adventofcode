#!/bin/bash
set -e

g++ -O2 -s -DNDEBUG -std=c++17 -Werror inc/catch_amalgamated.cpp src/main.cpp -c
g++ -O2 -s -DNDEBUG -std=c++17 -Werror -pthread \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode
