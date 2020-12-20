#!/bin/bash
set -e

g++ -std=c++17 -Werror \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode

./adventofcode $@