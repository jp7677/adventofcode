local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function equals(diagram, lights)
    for i, v in ipairs(diagram) do
        if lights[i] ~= v then return false end
    end
    return true
end

local function copy(buttons)
    local c = {}
    for i, v in ipairs(buttons) do
        c[i] = v
    end
    return c
end

local function diff(diagram, lights)
    local d = 0
    for i, _ in ipairs(diagram) do
        d = d + ((lights[i] ~= diagram[i]) and 1 or 0)
    end
    return d
end

local function switch(lights, buttons)
    local l = copy(lights)
    for _, v in ipairs(buttons) do
        l[v + 1] = not l[v + 1]
    end
    return l
end

local function push_buttons(diagram, lights, buttons, pushes, min_pushes)
    if min_pushes[1] <= pushes then return end
    if #buttons == 0 then return end

    if equals(diagram, lights) then
        if pushes < min_pushes[1] then min_pushes[1] = pushes end
        return
    end

    local sorted = {}
    for _, v in ipairs(buttons) do
        sorted[#sorted + 1] = { buttons = v, diff = diff(diagram, switch(lights, v)) }
    end

    table.sort(sorted, function(a, b) return a.diff < b.diff end)

    if sorted[1].diff == 0 then
        min_pushes[1] = pushes + 1
        return
    end

    for _, v in ipairs(sorted) do
        local l = switch(lights, v.buttons)
        local b = copy(buttons)
        for i, w in ipairs(b) do
            if w == v.buttons then
                table.remove(b, i)
            end
        end

        push_buttons(diagram, l, b, pushes + 1, min_pushes)
    end
end

local fn_day10_part1 = function()
    local input = util.load_input("10")

    local machines = fun.iter(input)
        :map(function(x)
            local d1 = string.find(x, ']')
            local d2 = string.find(x, '{')
            local diagram = {}
            for i = 1, d1 - 2 do
                if string.sub(x, i + 1, i + 1) == '#' then
                    diagram[i] = true
                else
                    diagram[i] = false
                end
            end
            local lights = {}
            for i = 1, d1 - 2 do
                lights[i] = false
            end
            local buttons = fun.iter(util.stringsplit(string.sub(x, d1 + 1, d2 - 1), ' '))
                :map(function(y)
                    return util.stringsplit(string.sub(y, 2, #y - 1), ',')
                end)
                :totable()
            local joltages = util.stringsplit(string.sub(x, d2 + 1, #x - 1), ',')
            return { diagram = diagram, lights = lights, buttons = buttons, joltages = joltages }
        end, input)
        :totable()

    local fewest_pushes = fun.foldl(function(acc, x)
        local min_pushes = { math.maxinteger }
        push_buttons(x.diagram, x.lights, x.buttons, 0, min_pushes)

        return acc + min_pushes[1]
    end, 0, machines)

    bstd.assert.same(542, fewest_pushes)
end

bstd.it("solves #day10 part 1, #ignore because very very slow", fn_day10_part1)
