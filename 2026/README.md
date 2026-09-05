# Advent of Code 2026 GLSL / Vulkan Compute

From <https://adventofcode.com/2026>. Better late than never ;)

## Minimum System requirements

- GCC 15
- Meson 1.11
- Vulkan SDK 1.4
- libcatch2 3.7 (`dnf install catch-devel` or `apt-get install libcatch2-dev`)
- libglm 1.0 (`dnf install glm-devel` or `apt-get install libglm-dev`)

Only Linux/GCC has been tested. Other platforms might just work though.

## Compile & run (Release)

```bash
meson setup --buildtype "release" buildDir
meson compile -C buildDir
DATA_PATH=./data SHADER_PATH=./buildDir/src/shaders ./buildDir/src/aoc2026
```

Use `DEVICE_INDEX` to select a different GPU than the first enumerated, e.g. `DEVICE_INDEX=1`.

The command line arguments from <https://github.com/catchorg/Catch2/blob/devel/docs/command-line.md> apply to the executable, e.g. use `./buildDir/src/aoc2026 --success -c "Day 01" -c "Part 1"` to run a single test only including showing verbose test results.

## Recompile (Debug)

```bash
meson setup --reconfigure --buildtype "debug" buildDir
```

## Recreate SPIR-V binary (Debug) and validate

Include `-g` in the `glslangValidator` arguments, like:

```bash
glslangValidator -g -V --target-env vulkan1.4 src/day01.comp.glsl -o ./buildDir/src/shaders/day01.spv
spirv-val --target-env vulkan1.4  ./buildDir/src/shaders/day01.spv
```
