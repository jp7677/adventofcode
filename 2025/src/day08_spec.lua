local bstd = require "busted"
local fun = require "fun"
local util = require "util"
local set  = require "set"

local function distance(a, b)
    return math.sqrt((a.x - b.x)^2 + math.abs(a.y - b.y)^2 + math.abs(a.z - b.z)^2)
end

local fn_day08_part1 = function()
    local input = util.load_input("08")

    local coords = fun.iter(input)
        :map(function(line)
            local x, y, z = table.unpack(util.stringsplit(line, ','))
            return { x = tonumber(x), y = tonumber(y), z = tonumber(z) }
        end)
        :totable()

    local distances = {}
    for _, coord in pairs(coords) do
        for _, other in pairs(coords) do
            if coord.x ~= other.x and coord.y ~= other.y and coord.z ~= other.z then
                distances[#distances + 1] = { a = coord, b = other, distance = distance(coord, other) }
            end
        end
    end

    table.sort(distances, function (a, b) return a.distance < b.distance end)
    fun.range(1, #distances, 2):each(function(x) distances[x] = nil end)
    distances = util.removenil(distances)

    local circuits = {}
    for i, c in pairs(coords) do
        circuits[i] = {}
        set.add(circuits[i], util.pack_coord3(c))
    end

    fun.iter(distances)
        :take(1000)
        :each(function(x)
            local a = util.pack_coord3(x.a)
            local b = util.pack_coord3(x.b)

            local c1 = {}
            local c2 = {}
            for _, c in pairs(circuits) do
                if set.contains(c, a) then c1 = c end
                if set.contains(c, b) then c2 = c end
            end

            if c1 ~= c2 then
                for _, y in pairs(set.values(c2)) do set.add(c1, y) end
                for _, y in pairs(set.values(c2)) do set.remove(c2, y) end
            end
        end)

    local sizes = {}
    for _, c in pairs(circuits) do sizes[#sizes + 1] = set.length(c) end
    table.sort(sizes, function (a, b) return a > b end)

    local result = fun.iter(sizes):take(3):product()

    bstd.assert.same(164475, result)
end

bstd.it("solves #day08 part 1", fn_day08_part1)
