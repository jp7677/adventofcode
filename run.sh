#!/bin/bash
g++ catch_amalgamated.o \
    main.o \
    src/day0.cpp \
    src/day1.cpp \
    src/day2.cpp \
    -o adventofcode

./adventofcode