local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day00 = function ()
    local input = util.load_input("02")[1]

    local ranges = util.stringsplit(input, ',')

    local invalid = fun.iter(ranges)
        :map(function (x)
            local p1, p2 = table.unpack(util.stringsplit(x, '-'))
            return fun.range(tonumber(p1), tonumber(p2))
                :filter(function (y) return #tostring(y) % 2 == 0 end)
                :filter(function (y)
                    local s = tostring(y)
                    local p = #s / 2
                    local a = string.sub(s, 1, p)
                    local b = string.sub(s, p + 1, #s)
                    return a == b
                end)
                :map(function(y) return tonumber(y) end)
                :sum()
        end)
        :sum()

    bstd.assert.same(19605500130, invalid)
end

bstd.it("solves day 00", fn_day00)
