#!/bin/bash
g++ catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode

./adventofcode