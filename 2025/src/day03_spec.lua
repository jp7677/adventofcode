local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function calculate_joltage(banks, size)
    return fun.iter(banks)
        :map(function(x)
            local idx = 0
            local largest = fun.range(size - 1, 0)
                :map(function(reserved)
                    local it = fun.take(#x - reserved, x):drop(idx)
                    local max = it:max()
                    idx = idx + it:index(max)
                    return max
                end)
                :totable()
            return tonumber(table.concat(largest))
        end)
        :sum()
end

local fn_day03_part1 = function()
    local input = util.load_input("03")

    local joltage = calculate_joltage(input, 2)

    bstd.assert.same(16854, joltage)
end

local fn_day03_part2 = function()
    local input = util.load_input("03")

    local joltage = calculate_joltage(input, 12)

    bstd.assert.same(167526011932478, joltage)
end

bstd.it("solves #day03 part 1", fn_day03_part1)
bstd.it("solves #day03 part 2", fn_day03_part2)
