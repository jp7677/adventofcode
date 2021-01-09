#!/bin/bash
set -e

cmake -DCMAKE_BUILD_TYPE=Release -B./cmake-build-release
cmake --build ./cmake-build-release --target all -- -j 9
