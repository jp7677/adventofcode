# Advent of Code C++

From <https://adventofcode.com/2020>. Likely to late for every day, but hopefully it's the intention that counts ;)

## System requirements

- build-essentials (gcc)
- cmake

Only Linux/gcc has been tested. It might just work though on other platforms.

## Compile & run (Release)

```bash
./build/make.sh
./cmake-build-release/adventofcode
```

The command line arguments from <https://github.com/catchorg/Catch2/blob/devel/docs/command-line.md#specifying-which-tests-to-run> apply to the executable, e.g. use `./cmake-build-release/adventofcode --success "Day 01 - Part 1 from https://adventofcode.com/2020/day/1"` to run a single test only including showing verbose test results.

## Recompile & run (Debug)

```bash
./build/run.sh
```

The command line arguments for the test runner (see above) are passed by the `run.sh` script to the executable.

## Other cool solutions

- <https://github.com/Dricus/adventofcode>
- <https://github.com/StefRave/advent-of-code-2020>
- <https://github.com/ErikSchierboom/advent-of-code>
