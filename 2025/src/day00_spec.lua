local bstd = require "busted"
local insp = require "inspect"
local fun = require "fun"
local util = require "util"

local fn_day00 = function ()
    local input = util.load_input("00")

    local fn_tonumber = function(x) return tonumber(x) end
    input = fun.map(fn_tonumber, input):totable()

    bstd.assert.same({0}, input)
    bstd.assert.same("{ 0 }", insp(input))
end

bstd.it("solves day 00", fn_day00)
