local bstd = require "busted"
local fun = require "fun"
local util = require "util"
local set = require "set"

local function load_coords()
    local input = util.load_input("07")

    local start
    local coords = {}
    local y_max
    for y, r in ipairs(input) do
        y_max = y
        for x = 1, #r do
            if r:sub(x, x) == 'S' then
                start = { x = x, y = y}
            end
            if r:sub(x, x) == '^' then
                coords[util.pack_coord({ x = x, y = y})] = true
            end
        end
    end
    return start, coords, y_max
end

local fn_day07_part1 = function()
    local start, coords, y_max = load_coords()

    local splitted = 0
    local acc = {}
    set.add(acc, start.x)
    for y = start.y, y_max, 2 do
        local splits = {}
        for i, v in pairs(acc) do
            if v ~= nil and coords[util.pack_coord({x = i, y = y})] then set.add(splits, i) end
        end

        for i, v in pairs(splits) do
            if v ~= nil then
                set.remove(acc, i)
                set.add(acc, i - 1)
                set.add(acc, i + 1)
            end
        end

        splitted = splitted + set.length(splits)
    end

    bstd.assert.same(1533, splitted)
end

local function traverse(start, coords, y_max)
    local acc = {}
    set.add(acc, start.x)

    local timelines = {}
    for y = start.y, y_max, 2 do
        local splits = {}
        for i, v in pairs(acc) do
            if v ~= nil and coords[util.pack_coord({x = i, y = y})] then set.add(splits, i) end
        end

        for i, v in pairs(splits) do
            if v ~= nil then
                set.remove(acc, i)
                set.add(acc, i - 1)
                timelines[#timelines + 1] = { x = i + 1, y = y }
            end
        end
    end

    return timelines
end

local fn_day07_part2 = function()
    local start, coords, y_max = load_coords()

    local timelines = { start }
    local total_timelines = 1
    while #timelines > 0 do
        local new_timelines = fun.iter(timelines)
            :map(function(t)
                return traverse(t, coords, y_max)
            end)
            :totable()

        if #new_timelines == 0 then break end

        timelines = fun.iter(new_timelines)
            :foldl(function(acc, x) return fun.chain(acc, x) end, {})
            :totable()
        total_timelines = total_timelines + #timelines
    end

    bstd.assert.same(40, total_timelines) -- sample input
end

bstd.it("solves #day07 part 1", fn_day07_part1)
bstd.it("solves #day07 part 2, #ignore because works only for small room", fn_day07_part2)
