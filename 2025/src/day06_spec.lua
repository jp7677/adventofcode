local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day06_part1 = function()
    local input = util.load_input("06")

    local rows = fun.iter(input)
        :map(function(x) return util.stringsplit(x, '%s') end)
        :totable()

    local problems = fun.range(#rows[1])
        :map(function(x)
            return fun.map(function(y) return y[x] end, rows):totable()
        end)
        :totable()

    local total = fun.iter(problems)
        :map(function(x)
            local op = x[#x]
            return fun.iter(x)
                :take(#x - 1)
                :foldl(function(acc, y)
                    if op == '+' then return acc + y end
                    if op == '*' then return acc * y end
                    error()
                end, op == '+' and 0 or 1)
        end)
        :sum()

    bstd.assert.same(3525371263915, total)
end

bstd.it("solves #day06 part 1", fn_day06_part1)
