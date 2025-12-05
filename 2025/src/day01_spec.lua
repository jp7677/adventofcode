local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day01_1 = function()
    local input = util.load_input("01")

    local rotations = fun.iter(input)
        :map(function(x) return { x:sub(1, 1), tonumber(x:sub(2)) } end)
        :foldl(function(acc, x)
            local current = acc[#acc]
            local direction = x[1]
            local clicks = x[2] % 100

            local point
            if direction == "R" then
                point = (current + clicks) % 100
                if point == 100 then point = 0 end
            else
                point = current - clicks
                if point < 0 then point = 100 + point end
            end

            table.insert(acc, point)
            return acc
        end, { 50 })

    local count = fun.iter(rotations)
        :filter(function(x) return x == 0 end)
        :length()

    bstd.assert.same(982, count)
end

local fn_day01_2 = function()
    local input = util.load_input("01")

    local rotations = fun.iter(input)
        :map(function(x) return { x:sub(1, 1), tonumber(x:sub(2)) } end)
        :foldl(function(acc, x)
            local direction = x[1]
            local clicks = x[2]
            local current = acc[#acc]

            fun.range(clicks):each(function(idx)
                local point = direction == "R" and current + 1 or current - 1
                if point == 100 then point = 0 end
                if point == -1 then point = 99 end
                current = point

                if point == 0 or idx == clicks then
                    table.insert(acc, point)
                end
            end)

            return acc
        end, { 50 })

    local count = fun.iter(rotations)
        :filter(function(x) return x == 0 end)
        :length()

    bstd.assert.same(6106, count)
end

bstd.it("solves #day01 part 1", fn_day01_1)
bstd.it("solves #day01 part 2", fn_day01_2)
