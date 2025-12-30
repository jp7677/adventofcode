local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function load_list()
    local input = util.load_input("11")

    return fun.iter(input)
        :map(function(x)
            local device = string.sub(x, 1, 3)
            local outputs = util.stringsplit(string.sub(x, 6), ' ')
            return { device = device, outputs = outputs }
        end)
        :totable()
end

local function follow(list, device, out, count, cache)
    count = count or 0
    cache = cache or {}

    if device == out then
        return count + 1
    end

    local connection = fun.iter(list)
        :filter(function(x) return x.device == device end)
        :totable()

    if #connection == 0 then return count end

    local count_cached = cache[device]
    if count_cached ~= nil then return count_cached end

    count_cached = fun.iter(connection[1].outputs)
        :foldl(function(acc, x)
            return acc + follow(list, x, out, count, cache)
        end, count)

    cache[device] = count_cached
    return count_cached
end

local fn_day11_part1 = function()
    local list = load_list()

    local count = follow(list, 'you', 'out')

    bstd.assert.same(423, count)
end

local fn_day11_part2 = function()
    local list = load_list()

    -- no connections from dac to fft
    local count1 = follow(list, 'svr', 'fft')
    local count2 = follow(list, 'fft', 'dac')
    local count3 = follow(list, 'dac', 'out')

    bstd.assert.same(333657640517376, count1 * count2 * count3)
end

bstd.it("solves #day11 part 1", fn_day11_part1)
bstd.it("solves #day11 part 2", fn_day11_part2)
