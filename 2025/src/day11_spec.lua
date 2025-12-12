local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function follow(list, device, count)
    if device == 'out' then
        count[1] = count[1] + 1
        return
    end

    local connection = fun.iter(list)
        :filter(function(x) return x.device == device end)
        :totable()

    if #connection == 0 then return end

    fun.each(function(x) follow(list, x, count) end, connection[1].outputs)
end

local fn_day11_part1 = function()
    local input = util.load_input("11")

    local list = fun.map(function(x)
        local device = string.sub(x, 1, 3)
        local outputs = util.stringsplit(string.sub(x, 6), ' ')
        return { device = device, outputs = outputs }
    end, input):totable()

    local count = { 0 }
    follow(list, 'you', count)

    bstd.assert.same(423, count[1])
end

bstd.it("solves #day11 part 1", fn_day11_part1)
