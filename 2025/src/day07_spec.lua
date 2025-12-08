local bstd = require "busted"
local util = require "util"
local set = require "set"

local function load_coords()
    local input = util.load_input("07")

    local start
    local coords = {}
    local y_max
    for y, row in ipairs(input) do
        y_max = y
        for x = 1, #row do
            if row:sub(x, x) == 'S' then
                start = { x = x, y = y }
            end
            if row:sub(x, x) == '^' then
                set.add(coords, util.pack_coord({ x = x, y = y }))
            end
        end
    end
    return start, coords, y_max
end

local fn_day07 = function()
    local start, coords, y_max = load_coords()

    local splits = 0
    local timelines = 0

    local beams = {}
    beams[start.x] = 1
    for y = start.y, y_max, 2 do
        local splitters = {}
        for i, _ in pairs(beams) do
            if set.contains(coords, util.pack_coord({x = i, y = y})) then set.add(splitters, i) end
        end

        for i, _ in pairs(splitters) do
            local tl0 = beams[i]
            local tl1 = beams[i - 1]
            local tl2 = beams[i + 1]

            if tl0 == nil then tl0 = 0 end
            if tl1 == nil then tl1 = 0 end
            if tl2 == nil then tl2 = 0 end

            beams[i] = nil
            beams[i - 1] = tl0 + tl1
            beams[i + 1] = tl0 + tl2
        end

        splits = splits + set.length(splitters)
    end

    for _, v in pairs(beams) do
        timelines = timelines + v
    end

    bstd.assert.same(1533, splits)
    bstd.assert.same(10733529153890, timelines)
end

bstd.it("solves #day07 part 1 & part 2", fn_day07)
