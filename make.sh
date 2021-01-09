#!/bin/bash
set -e

g++ -O2 -s -DNDEBUG -std=c++17 -Wall -Wextra -Werror inc/catch_amalgamated.cpp src/main.cpp -c
g++ -O2 -s -DNDEBUG -std=c++17 -Wall -Wextra -Werror -pthread \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode
