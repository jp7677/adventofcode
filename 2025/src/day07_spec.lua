local bstd = require "busted"
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

local function set_add(set, key)
    set[key] = true
end

local function set_remove(set, key)
    set[key] = nil
end

-- local function set_contains(set, key)
--     return set[key] ~= nil
-- end

local function set_values(set)
  local copy = {}
  for i, v in pairs(set) do
    if v ~= nil then copy[#copy + 1] = i end
  end
  return copy
end

local fn_day07_part1 = function()
    local start, coords, y_max = load_coords()

    local init = {}
    set_add(init, dec(start).x)

    local splitted = 0
    fun.range(1, y_max, 2)
        :foldl(function(acc, y)
            local splits = fun.iter(set_values(acc))
                :filter(function(b) return fun.any(function(c) return c == enc({x = b, y = y}) end, coords) end)
                :totable()

            fun.each(function(s)
                set_remove(acc, s)
                set_add(acc, s - 1)
                set_add(acc, s + 1)
            end, splits)

            splitted = splitted + #splits
            return acc
        end, init)

    bstd.assert.same(1533, splitted)
end

bstd.it("solves #day07 part 1", fn_day07_part1)
