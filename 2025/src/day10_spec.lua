local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function copy(buttons)
    local c = {}
    for i, v in ipairs(buttons) do
        c[i] = v
    end
    return c
end

-- from https://stackoverflow.com/a/59469841
local function combinations(s, p, t, acc)
    p = p or 1
    acc = acc or {}
    t = t or {}

    if p == #s + 1 then
        table.insert(acc, t)
    else
        combinations(s, p + 1, t, acc)
        local t2 = copy(t)
        table.insert(t2, s[p])
        combinations(s, p + 1, t2, acc)
    end
    return acc
end

local function load_machines()
    local input = util.load_input("10")

    return fun.iter(input)
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
            local diagram_packed = fun.foldl(function(acc, y)
                return (acc << 1) + (y and 1 or 0)
            end, 0, diagram)

            local lights = fun.map(function() return 0 end, diagram):totable()
            local lights_packed = 0

            local buttons = fun.iter(util.stringsplit(string.sub(x, d1 + 1, d2 - 1), ' '))
                :map(function(y)
                    return fun.iter(util.stringsplit(string.sub(y, 2, #y - 1), ','))
                        :map(function(b) return tonumber(b) end)
                        :totable()
                end)
                :totable()
            local buttons_packed = fun.iter(buttons)
                :map(function(b)
                    return fun.iter(fun.range(0, #diagram - 1))
                        :foldl(function(acc, y)
                            return (acc << 1) + (fun.index(y, b) and 1 or 0)
                        end, 0)
                end)
                :totable()
            local buttons_packed_combinations = combinations(buttons_packed)

            local joltages = fun.iter(util.stringsplit(string.sub(x, d2 + 1, #x - 1), ','))
                :map(function(j) return tonumber(j) end)
                :totable()

            return {
                diagram = diagram,
                diagram_packed = diagram_packed,
                lights = lights,
                lights_packed = lights_packed,
                buttons = buttons,
                buttons_packed = buttons_packed,
                buttons_packed_combinations = buttons_packed_combinations,
                joltages = joltages
            }
        end)
        :totable()
end

local function unpack_buttons(buttons, buttons_unpacked, buttons_packed)
    local c = {}
    for i, b in ipairs(buttons_packed) do
        for _, button in ipairs(buttons) do
            if b == button then table.insert(c, buttons_unpacked[i]) end
        end
    end
    return c
end

local function start_machine(machine, diagram_packed)
    local configurations = {}
    for _, buttons_packed in ipairs(machine.buttons_packed_combinations) do
        local v = 0
        for _, b in ipairs(buttons_packed) do
            v = v ~ b -- XOR
        end
        if v == diagram_packed then
            local buttons = unpack_buttons(buttons_packed, machine.buttons, machine.buttons_packed)
            table.insert(configurations, { buttons_packed = buttons_packed, buttons = buttons })
        end
    end
    return configurations
end

local fn_day10_part1 = function()
    local machines = load_machines()

    local min_pushes = fun.foldl(function(acc, machine)
        local configurations = start_machine(machine, machine.diagram_packed)
        local min_pushes = fun.iter(configurations)
            :map(function(x) return #x.buttons end)
            :min()
        return acc + min_pushes
    end, 0, machines)

    bstd.assert.same(542, min_pushes)
end

local function concat(t1, v)
    local t = copy(t1)
    table.insert(t, 1, v)
    return t
end

local function joltages_to_diagram(joltage)
    local diagram = {}
    for i = 1, #joltage do
        if joltage[i] % 2 == 0 then
            diagram[i] = false
        else
            diagram[i] = true
        end
    end
    local diagram_packed = 0
    for _, v in ipairs(diagram) do
        diagram_packed = (diagram_packed << 1) + (v and 1 or 0)
    end
    return diagram_packed
end

local function buttons_to_joltages(machine, buttons)
    local joltages = {}
    for i = 1, #machine.joltages do joltages[i] = 0 end
    for _, b in ipairs(buttons) do
        for _, b1 in ipairs(b) do
            joltages[b1 + 1] = joltages[b1 + 1] + 1
        end
    end
    return joltages
end

local function diff_joltages(j1, j2)
    local joltages = {}
    for i, v in ipairs(j1) do
        joltages[i] = v - j2[i]
    end
    return joltages
end

local function valid_and_even_joltages(joltages)
    for _, v in ipairs(joltages) do
        if v < 0 or v % 2 ~= 0 then return false end
    end
    return true
end

local function halve_joltages(j1)
    local joltages = {}
    for i, v in ipairs(j1) do
        joltages[i] = math.floor(v / 2)
    end
    return joltages
end

local function calc_total(pushes)
    local total = 0
    for _, v in ipairs(pushes) do
        total = (total * 2) + v
    end
    return total
end

local function zero_joltages(joltages)
    for _, v in ipairs(joltages) do
        if v ~= 0 then return false end
    end
    return true
end

local function configure_machine(machine, joltages, pushes, total_pushes, cache)
    if valid_and_even_joltages(joltages) then
        configure_machine(machine, halve_joltages(joltages), concat(pushes, 0), total_pushes, cache)
    end

    local diagram_packed = joltages_to_diagram(joltages)
    local configurations = cache[diagram_packed]
    if configurations == nil then
        configurations = start_machine(machine, diagram_packed)
        cache[diagram_packed] = configurations
    end

    for _, s in ipairs(configurations) do
        local j = diff_joltages(joltages, buttons_to_joltages(machine, s.buttons))
        if valid_and_even_joltages(j) then
            j = halve_joltages(j)
            local p = concat(pushes, #s.buttons)
            local total = calc_total(p)
            if total < total_pushes[1] then
                if zero_joltages(j) then
                    total_pushes[1] = total
                else
                    configure_machine(machine, j, p, total_pushes, cache)
                end
            end
        end
    end

    return total_pushes
end

local fn_day10_part2 = function()
    local machines = load_machines()

    local min_pushes = fun.foldl(function(acc, machine)
        local pushes = configure_machine(machine, machine.joltages, {}, { math.maxinteger }, {})
        return acc + pushes[1]
    end, 0, machines)

    bstd.assert.same(20871, min_pushes)
end

bstd.it("solves #day10 part 1", fn_day10_part1)
-- part 2 taken from
-- https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
bstd.it("solves #day10 part 2", fn_day10_part2)
