local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local directions = {
    { x = -1, y = -1 }, { x = 0, y = -1 }, { x = 1, y = -1 },
    { x = -1, y =  0 },                    { x = 1, y =  0 },
    { x = -1, y =  1 }, { x = 0, y =  1 }, { x = 1, y =  1 }
}

local function load_coords()
    local input = util.load_input("04")

    local coords = {}
    for y, r in ipairs(input) do
        for x = 1, #r do
            if r:sub(x, x) == '@' then
                table.insert(coords, {x = x, y = y})
            end
        end
    end
    return coords
end

local function find_removable(coords)
    return fun.iter(coords)
        :filter(function(coord)
            return fun.iter(directions)
                :foldl(function(acc, direction)
                    local adj = { x = coord.x + direction.x, y = coord.y + direction.y }
                    if fun.iter(coords):any(function(c) return c.x == adj.x and c.y == adj.y end) then
                        return acc + 1
                    end
                    return acc
                end, 0) < 4
        end)
        :totable()
end

local fn_day04_part1 = function()
    local coords = load_coords()

    local removable = find_removable(coords)

    bstd.assert.same(1587, #removable)
end

local fn_day04_part2 = function()
    local coords = load_coords()
    local coords_count = #coords

    local removable = find_removable(coords)

    while #removable > 0 do
        coords = fun.iter(coords)
            :filter(function(c)
                return not fun.iter(removable):any(function(r) return c.x == r.x and c.y == r.y end)
            end)
            :totable()
        removable = find_removable(coords)
    end

    bstd.assert.same(8946, coords_count - #coords)
end

bstd.it("solves day 04 part 1 (#ignore because way to slow ...)", fn_day04_part1)
bstd.it("solves day 04 part 2 (#ignore because way to slow ...)", fn_day04_part2)
