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

local fn_day06_part2 = function()
    local input = util.load_input("06")

    local indicies = fun.iter(input)
        :drop(#input - 1)
        :map(function(x)
            local indicies = {}
            for i = 1, #x do
                local c = x:sub(i,i)
                if c == '+' or c == '*' then table.insert(indicies, i) end
            end
            table.insert(indicies, #x + 2)
            return indicies
        end)
        :totable()[1]

    local ranges = {}
    for i = 1, #indicies - 1 do
        ranges[i] = { indicies[i], indicies[i + 1] - 2 }
    end

    local rows = fun.iter(input)
        :map(function(x) return fun.map(function(y) return x:sub(y[1], y[2]) end, ranges):totable() end)
        :totable()

    local problems = fun.range(#rows[1])
        :map(function(x) return fun.map(function(y) return y[x] end, rows):totable() end)
        :map(function(column)
            local op = column[#column]:sub(1, 1)
            local numbers = fun.range(#column[1])
                :map(function(x)
                    return fun.iter(column)
                        :take(#column - 1)
                        :map(function(y) return y:sub(x,x) end)
                        :totable()
                end)
                :map(function(x) return table.concat(x) end)
                :totable()
            return fun.chain(numbers, op):totable()
        end)
        :totable()

    local total = fun.iter(problems)
        :map(function(x)
            local op = x[#x]
            return fun.iter(x)
                :take(#x - 1)
                :foldl(function(acc, y)
                    if op == '+' then return acc + tonumber(y) end
                    if op == '*' then return acc * tonumber(y) end
                    error()
                end, op == '+' and 0 or 1)
        end)
        :sum()

    bstd.assert.same(6846480843636, total)
end

bstd.it("solves #day06 part 1", fn_day06_part1)
bstd.it("solves #day06 part 2", fn_day06_part2)
