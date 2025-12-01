local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day01_1 = function ()
    local rotations = util.load_input("01")

    rotations = fun
        .map(function(x) return { x:sub(1, 1), tonumber(x:sub(2)) } end, rotations)
        :foldl(function(acc, x)
            local current = acc[#acc]
            local direction = x[1]
            local clicks = x[2] % 100

            local point
            if direction == "R" then
                point = (current + clicks) % 100
                if point == 100 then
                    point = 0
                end
            else
                point = current - clicks
                if point < 0 then
                    point = 100 + point
                end
            end

            table.insert(acc, point)
            return acc
        end, { 50 })

    local zeros = fun
        .filter(function(x) return x == 0 end, rotations)
        :totable()

    bstd.assert.same(982, #zeros)
end

bstd.it("solves day 01 part 1", fn_day01_1)
