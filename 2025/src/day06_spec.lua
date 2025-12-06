local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function flip_diagonal(rows)
    return fun.range(#rows[1])
        :map(function(x)
            return fun.map(function(y) return y[x] end, rows):totable()
        end)
        :totable()
end

local function calculate_total(problems)
    return fun.iter(problems)
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
end

local fn_day06_part1 = function()
    local input = util.load_input("06")

    local rows = fun.iter(input)
        :map(function(x) return util.stringsplit(x, '%s') end)
        :totable()

    local problems = flip_diagonal(rows)

    local total = calculate_total(problems)

    bstd.assert.same(3525371263915, total)
end

local fn_day06_part2 = function()
    local input = util.load_input("06")
    local ops = util.stringtotable(input[#input])

    local indicies = fun.chain(fun.indexes('*', ops), fun.indexes('+', ops), { #ops + 2 }):totable()
    table.sort(indicies)

    local ranges = fun.zip(indicies, fun.iter(indicies):drop(1))
        :map(function(x, y) return { x, y - 2} end)
        :totable()

    local rows = fun.iter(input)
        :map(function(x)
            return fun.map(function(y) return x:sub(y[1], y[2]) end, ranges):totable()
        end)
        :totable()

    local problems = fun.iter(flip_diagonal(rows))
        :map(function(x)
            local op = x[#x]:sub(1, 1)
            local column = fun.iter(x)
                :take(#x - 1)
                :map(function(y) return util.stringtotable(y) end)
                :totable()
            local numbers = fun.iter(flip_diagonal(column))
                :map(function(y) return table.concat(y) end)
                :totable()

            return fun.chain(numbers, op):totable()
        end)
        :totable()

    local total = calculate_total(problems)

    bstd.assert.same(6846480843636, total)
end

bstd.it("solves #day06 part 1", fn_day06_part1)
bstd.it("solves #day06 part 2", fn_day06_part2)
