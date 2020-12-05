#!/bin/bash
g++ inc/catch_amalgamated.cpp src/main.cpp -c
g++ catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode
