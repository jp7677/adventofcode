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

local function switch(lights, buttons)
    local l = copy(lights)
    for _, v in ipairs(buttons) do
        l[v + 1] = not l[v + 1]
    end
    return l
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
                    return fun.iter(util.stringsplit(string.sub(y, 2, #y - 1), ','))
                        :map(function(b) return tonumber(b) end)
                        :totable()
                end)
                :totable()
            local joltages = util.stringsplit(string.sub(x, d2 + 1, #x - 1), ',')
            return { diagram = diagram, lights = lights, buttons = buttons, joltages = joltages }
        end, input)
        :totable()

    local fewest_pushes = fun.foldl(function(acc, machine)
        local min_pushes = math.maxinteger
        local states = { { lights = machine.lights, tested_buttons = {} } }

        for pushes = 1, #machine.buttons do
            local new_states = {}
            for _, state in ipairs(states) do
                local remaining_buttons = fun.iter(machine.buttons)
                    :filter(function(x)
                        return not fun.any(function(y)
                            return table.concat(x) == table.concat(y)
                        end, state.tested_buttons)
                    end)
                    :totable()

                for _, v in ipairs(remaining_buttons) do
                    local lights = switch(state.lights, v)
                    if equals(lights, machine.diagram) then
                        min_pushes = pushes
                        goto found
                    else
                        local tested = copy(state.tested_buttons)
                        table.insert(tested, v)
                        table.insert(new_states, { lights = lights, tested_buttons = tested })
                    end
                end
            end
            states = new_states
        end

        ::found::
        return acc + min_pushes
    end, 0, machines)

    bstd.assert.same(542, fewest_pushes)
end

bstd.it("solves #day10 part 1", fn_day10_part1)
