local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day12_1 = function()
    local input = util.load_input("12")

    local shapes = fun.iter(input)
        :filter(function(x) return string.find(x, '#') end)
        :totable()

    local tiles = fun.range(1, #shapes, 3)
        :map(function(x)
            return fun.chain(shapes[x], shapes[x + 1], shapes[x + 2])
                :filter(function(y) return string.find(y, '#') end)
                :length()
        end)
        :totable()

    local regions = fun.iter(input)
        :filter(function(x) return string.find(x, 'x') end)
        :map(function(r)
            local x = tonumber(string.sub(r, 1, 2))
            local y = tonumber(string.sub(r, 4, 5))
            local counts = fun.iter(util.stringsplit(string.sub(r, 5), ' '))
                :map(function(c) return tonumber(c) end)
                :totable()
            return { x = x, y = y, counts = counts }
        end)
        :totable()

    local count = fun.iter(regions)
        :filter(function(r)
            local max = r.x * r.y
            local needed = 0
            for i, v in pairs(tiles) do
                needed = needed + (v * r.counts[i])
            end
            return needed <= max
        end)
        :length()

    bstd.assert.same(492, count)
end

bstd.it("solves #day12 part 1", fn_day12_1)
