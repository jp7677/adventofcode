local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function follow(list, device, count)
    if device == 'out' then return count + 1 end

    local connection = fun.iter(list)
        :filter(function(x) return x.device == device end)
        :totable()

    if #connection == 0 then return count end

    return fun.iter(connection[1].outputs)
        :foldl(function(acc, x)
            return acc + follow(list, x, count)
        end, count)
end

local fn_day11_part1 = function()
    local input = util.load_input("11")

    local list = fun.iter(input)
        :map(function(x)
            local device = string.sub(x, 1, 3)
            local outputs = util.stringsplit(string.sub(x, 6), ' ')
            return { device = device, outputs = outputs }
        end)
        :totable()

    local count = follow(list, 'you', 0)

    bstd.assert.same(423, count)
end

bstd.it("solves #day11 part 1", fn_day11_part1)
