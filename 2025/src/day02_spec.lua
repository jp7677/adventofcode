local bstd = require "busted"
local insp = require "inspect"
local fun = require "fun"
local util = require "util"

local chuncked = function(str, size)
    local res = {}
    if #str % size ~= 0 then return res end

    local parts = #str / size
    for i = 0, parts - 1 do
        local start = (i * size) + 1
        table.insert(res, string.sub(str, start, start + size - 1))
    end
    return res
end

bstd.it("chuncked", function()
    bstd.assert.same(insp(chuncked('1', 1)), '{ "1" }')
    bstd.assert.same(insp(chuncked('1122', 2)), '{ "11", "22" }')
    bstd.assert.same(insp(chuncked('112233', 2)), '{ "11", "22", "33" }')
    bstd.assert.same(insp(chuncked('112233', 3)), '{ "112", "233" }')
end)

local sum_of_invalid = function(fn_filter)
    local input = util.load_input("02")
    local ranges = util.stringsplit(input[1], ',')

    return fun.iter(ranges)
        :foldl(function(acc, x)
            local p1, p2 = table.unpack(util.stringsplit(x, '-'))
            return acc + fun.range(tonumber(p1), tonumber(p2))
                :filter(function (y) return fn_filter(y) end)
                :sum(function(y) return y end)
        end, 0)
end

local fn_day00_part1 = function()
    local invalid = sum_of_invalid(function (y)
        local s = tostring(y)
        if #s % 2 ~= 0 then return false end
        local c1, c2 = table.unpack(chuncked(s, #s / 2))
        return c1 == c2
    end)

    bstd.assert.same(19605500130, invalid)
end

local fn_day00_part2 = function()
    local invalid = sum_of_invalid(function(y)
        local s = tostring(y)
        for p = 1, #s / 2 do
            if #s > 1 and #s % p == 0 then
                local chuncks = chuncked(s, p)
                local same = fun.all(function(a) return a == chuncks[1] end, chuncks)
                if same == true then return true end
            end
        end
    end)

    bstd.assert.same(36862281418, invalid)
end

bstd.it("solves #day02 part 1", fn_day00_part1)
bstd.it("solves #day02 part 1", fn_day00_part2)
