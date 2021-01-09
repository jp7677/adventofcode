#!/bin/bash
set -e

g++ -std=c++17 -Wall -Wextra -Werror -pthread \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode

./adventofcode $@