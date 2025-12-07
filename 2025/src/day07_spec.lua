local bstd = require "busted"
local insp = require "inspect"
local fun = require "fun"
local util = require "util"

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
    local input = util.load_input("07")

    local start
    local coords = {}
    local y_max
    for y, r in ipairs(input) do
        y_max = y
        for x = 1, #r do
            if r:sub(x, x) == 'S' then
                start = enc({ x = x, y = y})
            end
            if r:sub(x, x) == '^' then
                coords[enc({ x = x, y = y})] = true
            end
        end
    end
    return start, coords, y_max
end

local fn_day07_part1 = function()
    local start, coords, y_max = load_coords()

    local splitted = 0
    fun.range(2, y_max)
        :foldl(function(acc, y)
            local beams = fun.iter(acc)
                :map(function(b) return dec(b) end)
                :map(function(b) return { x = b.x, y = y} end)
                :totable()

            local splits = fun.iter(beams)
                :filter(function(b)
                    return fun.any(function(z) return z == enc(b) end, coords)
                end)
                :totable()
            
            local other_beams = fun.iter(beams)
                :filter(function(b) return fun.all(function(s) return s.x ~= b.x end, splits) end)
                :totable()

            local new_beams = fun.iter(splits)
                :map(function(b) return { { x = b.x - 1, y = y }, { x = b.x + 1, y = y } } end)
                :reduce(function(acc, x) return fun.chain(acc, x) end, {})

            local all = fun.chain(other_beams, new_beams)
                :map(function(b) return enc(b) end)
                :totable()

            splitted = splitted + #splits
            return util.dedup(all)
        end, { start })

    bstd.assert.same(1533, splitted)
end

bstd.it("solves #day07 part 1", fn_day07_part1)
