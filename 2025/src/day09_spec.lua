local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day09_part1 = function()
    local input = util.load_input("09")

    local coords = fun.iter(input)
    :map(function(line)
        local x, y, z = table.unpack(util.stringsplit(line, ','))
        return { x = tonumber(x), y = tonumber(y), z = tonumber(z) }
    end)
    :totable()

    local areas = {}
    for _, coord in pairs(coords) do
        for _, other in pairs(coords) do
            if coord.x ~= other.x and coord.y ~= other.y  then
                areas[#areas + 1] = math.abs(coord.x - other.x + 1) * math.abs(coord.y - other.y + 1)
            end
        end
    end

    table.sort(areas, function (a, b) return a > b end)

    bstd.assert.same(4771532800, areas[1])
end

bstd.it("solves #day09 part 1", fn_day09_part1)
