local bstd = require "busted"
local fun = require "fun"
local util = require "util"

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
                joltages = joltages
            }
        end, input)
        :totable()
end

local function copy(buttons)
    local c = {}
    for i, v in ipairs(buttons) do
        c[i] = v
    end
    return c
end

local function start_machine(machine)
    local min_pushes = math.maxinteger
    local states = { { lights = machine.lights_packed, tested_buttons = {} } }

    for pushes = 1, #machine.buttons do
        local next_states = {}
        for _, state in ipairs(states) do
            local untested_buttons = {}
            for _, b in ipairs(machine.buttons_packed) do
                for _, t in ipairs(state.tested_buttons) do
                    if b == t then
                        goto next
                    end
                end
                table.insert(untested_buttons, b)
                ::next::
            end

            for _, v in ipairs(untested_buttons) do
                local lights = state.lights ~ v
                if lights == machine.diagram_packed then
                    table.insert(state.tested_buttons, v)
                    min_pushes = pushes
                    goto machine_started
                else
                    local tested = copy(state.tested_buttons)
                    table.insert(tested, v)
                    table.insert(next_states, { lights = lights, tested_buttons = tested })
                end
            end
        end
        states = next_states
    end

    ::machine_started::
    return { pushes = min_pushes }
end

local fn_day10_part1 = function()
    local machines = load_machines()

    local min_pushes = fun.foldl(function(acc, machine)
        return acc + start_machine(machine).pushes
    end, 0, machines)

    bstd.assert.same(542, min_pushes)
end

bstd.it("solves #day10 part 1", fn_day10_part1)
