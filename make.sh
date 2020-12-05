#!/bin/bash
g++ inc/catch_amalgamated.cpp -c
g++ catch_amalgamated.o \
    src/main.cpp \
    src/day0.cpp \
    src/day1.cpp \
    -o adventofcode
