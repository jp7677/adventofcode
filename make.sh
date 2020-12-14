#!/bin/bash
g++ -std=c++17 inc/catch_amalgamated.cpp src/main.cpp -c
g++ -std=c++17 \
    catch_amalgamated.o \
    main.o \
    src/day*.cpp \
    -o adventofcode
