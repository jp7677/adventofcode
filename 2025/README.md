# Advent of Code 2024 Lua

From <https://adventofcode.com/2025>. Better late than never ;)

## System requirements

- LuaJIT 2.1 

Lua 5.4 should also "just work". Only Linux has been tested. Other platforms might just work though.

## Setup

```bash
luarocks init --lua-versions 5.4
luarocks install luacheck
luarocks install busted
luarocks install inspect
luarocks install --dev fun
```

## run tests

```bash
./lua_modules/bin/busted
```

Use `./lua_modules/bin/busted --tags="day01"` for a specific day.
