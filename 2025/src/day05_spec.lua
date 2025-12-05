local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local fn_day05_part1 = function()
    local input = util.load_input("05")
    local delim = fun.iter(input):index('')

    local ranges = fun.iter(input)
        :take(delim - 1)
        :map(function(x)
            local p = util.stringsplit(x, '-')
            return { tonumber(p[1]), tonumber(p[2]) }
        end)
        :totable()

    local ids = fun.iter(input)
        :drop(delim)
        :map(function(x) return tonumber(x) end)
        :totable()

    local fresh = fun.iter(ids)
        :filter(function(x)
            return fun.iter(ranges):any(function(y) return x >= y[1] and x <= y[2] end)
        end)
        :length()

    bstd.assert.same(773, fresh)
end

bstd.it("solves #day05 part 1", fn_day05_part1)
