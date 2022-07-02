# Advent of Code 2020 C++

From <https://adventofcode.com/2020>. Better late than never ;)

## System requirements

- build-essentials (gcc, libunwind-dev libdw-dev)
- cmake

Only Linux/gcc has been tested. Other platforms might just work though.

## Compile & run (Release)

```bash
./build/make.sh
./cmake-build-release/adventofcode
```

The command line arguments from <https://github.com/catchorg/Catch2/blob/devel/docs/command-line.md#specifying-which-tests-to-run> apply to the executable, e.g. use `./cmake-build-release/adventofcode --success "Day 01 - Part 1*"` to run a single test only including showing verbose test results.

## Recompile & run (Debug)

```bash
./build/run.sh
```

The command line arguments for the test runner (see above) are passed by the `run.sh` script to the executable.

## Results

Enjoying all the stars...

![Final AoC map](https://github.com/jp7677/adventofcode/raw/main/2020/map.png)
