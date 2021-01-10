#!/bin/bash
set -e

if [ ! -d "./cmake-build-debug" ]
then
    cmake -DCMAKE_BUILD_TYPE=Debug -B./cmake-build-debug
fi

cmake --build ./cmake-build-debug --target all -- -j 9
./cmake-build-debug/adventofcode "$@"