#!/bin/bash
set -e

cmake --build ./cmake-build-release --target all -- -j 9
./cmake-build-release/adventofcode "$@"