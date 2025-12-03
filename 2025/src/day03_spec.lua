local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day03_part1 = function()
    local input = util.load_input("03")

    local joltage = fun.iter(input)
        :map(function(x)
            local it = fun.iter(x)
            local b1 = it:take(#x - 1):max(x)
            local idx = it:index(b1)
            local b2 = it:drop(idx):max(x)
            return tonumber(b1 .. b2)
        end)
        :sum()

    bstd.assert.same(16854, joltage)
end

bstd.it("solves day 03 part 1", fn_day03_part1)
