#!/bin/bash
g++ -std=c++17 \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode

./adventofcode