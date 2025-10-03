local bstd = require "busted"
local insp = require "inspect"
local fun = require "fun"
local util = require "util"

bstd.describe("day 00", function()
    bstd.it("solves part 1", function()
        local input = util.load_input("00")

        local fn_tonumber = function(x) return tonumber(x) end
        input = fun.map(fn_tonumber, input):totable()

        bstd.assert.are.same({0}, input)
        bstd.assert.are.same("{ 0 }", insp(input))
    end)
end)
