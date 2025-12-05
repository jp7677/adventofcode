local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local directions = {
    { x = -1, y = -1 }, { x = 0, y = -1 }, { x = 1, y = -1 },
    { x = -1, y =  0 },                    { x = 1, y =  0 },
    { x = -1, y =  1 }, { x = 0, y =  1 }, { x = 1, y =  1 }
}

local function enc(coord)
    return (coord.x << 8) + coord.y
end

local function dec(index)
    local y = index & 0x00ff
    local x = index >> 8
    return { x = x, y = y}
end

bstd.it("serializes coordinates", function()
    bstd.assert.same(257, enc({ x = 1, y = 1}))
    bstd.assert.same({ x = 1, y = 1}, dec(257))
end)

local function load_coords()
    local input = util.load_input("04")

    local coords = {}
    for y, r in ipairs(input) do
        for x = 1, #r do
            if r:sub(x, x) == '@' then
                coords[enc({ x = x, y = y})] = true
            end
        end
    end
    return coords
end

local function find_removable(coords)
    return fun.iter(coords)
        :filter(function(c)
            local coord = dec(c)
            return fun.iter(directions)
                :foldl(function(acc, direction)
                    local adj = enc({ x = coord.x + direction.x, y = coord.y + direction.y })
                    if coords[adj] then return acc + 1 end
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

local function idx_length(t)
    return fun.iter(t):length()
end

local fn_day04_part2 = function()
    local coords = load_coords()
    local coords_count = idx_length(coords)

    local removable = find_removable(coords)

    while #removable > 0 do
        fun.iter(coords)
            :filter(function(c)
                return fun.iter(removable):any(function(r) return c == r end)
            end)
            :each(function(i) coords[i] = nil end)

        removable = find_removable(coords)
    end

    bstd.assert.same(8946, coords_count - idx_length(coords))
end

bstd.it("solves #day04 part 1", fn_day04_part1)
bstd.it("solves #day04 part 2", fn_day04_part2)
