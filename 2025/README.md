# Advent of Code 2024 Lua

From <https://adventofcode.com/2025>. Better late than never ;)

## System requirements

- LuaJIT 2.1 

Lua 5.4 should also "just work". Only Linux has been tested. Other platforms might just work though.

## Compile & run

```bash
luarocks init aoc2025-0-1.rockspec
./luarocks install --only-deps aoc2025-0-1.rockspec
LUA_INIT=@src/setup.lua ./lua_modules/bin/busted
```
