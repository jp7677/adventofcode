local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function load_coords()
    local input = util.load_input("09")

    return fun.iter(input)
        :map(function(line)
            local x, y = table.unpack(util.stringsplit(line, ','))
            return { x = tonumber(x), y = tonumber(y) }
        end)
        :totable()
end

local function calc_area(a, b)
    return (math.abs(a.x - b.x) + 1) * (math.abs(a.y - b.y) + 1)
end

local fn_day09_part1 = function()
    local coords = load_coords()

    local areas = {}
    for _, coord in pairs(coords) do
        for _, other in pairs(coords) do
            if coord.x ~= other.x and coord.y ~= other.y  then
                areas[#areas + 1] = calc_area(coord, other)
            end
        end
    end

    table.sort(areas, function (a, b) return a > b end)

    bstd.assert.same(4771532800, areas[1])
end

local fn_day09_part2 = function()
    local coords = load_coords()

    -- our fix point from the data which "divides" the oval, not cool to hardcode those :(
    local other1 = { x = 94872, y = 48511 }
    local other2 = { x = 94872, y = 50262 }

    local areas1 = fun.iter(coords)
        :filter(function(x) return x.y < other1.y end)
        :map(function(coord)
            if not fun.any(function(x)
                    return ((x.x > coord.x and x.x < other1.x) and (x.y > coord.y and x.y < other1.y))
                end, coords)
            then
                return calc_area(coord, other1)
            else
                return 0
            end
        end)

    local areas2 = fun.iter(coords)
        :filter(function(x) return x.y > other2.y end)
        :map(function(coord)
            if not fun.any(function(x)
                    return ((x.x < other2.x and x.x > coord.x) and (x.y < coord.y and x.y > other2.y))
                end, coords)
            then
                return calc_area(coord, other2)
            else
                return 0
            end
        end)

    local max_area = fun.max(fun.chain(areas1, areas2))

    bstd.assert.same(1544362560, max_area)
end

bstd.it("solves #day09 part 1", fn_day09_part1)
bstd.it("solves #day09 part 2", fn_day09_part2)
