local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function calculate_joltage(banks, size)
    return fun.iter(banks)
        :map(function(x)
            local largest = {}
            local remaining = x
            fun.range(size - 1, 0):each(function(reserved)
                local it = fun.iter(remaining)
                local max = it:take(#remaining - reserved):max(x)
                local idx = it:index(max)
                remaining = it:drop(idx):totable()
                table.insert(largest, max)
            end)
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

bstd.it("solves day 03 part 1", fn_day03_part1)
bstd.it("solves day 03 part 2", fn_day03_part2)
