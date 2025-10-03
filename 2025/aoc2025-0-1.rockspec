package = "aoc2025"
version = "0-1"
source = {
   url = "git+ssh://git@github.com/jp7677/adventofcode.git"
}
description = {
   summary = "AOC2025",
   homepage = "https://github.com/jp7677/adventofcode/tree/main/2025/",
   license = "MIT"
}
dependencies = {
   "lua >= 5.1",
   "luacheck",
   "busted",
   "inspect"
}
build = {
   type = "builtin",
   modules = {
      ["main"] = "src/main.lua"
   }
}
