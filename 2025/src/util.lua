local fun = require "fun"

local util = {}

-- from https://github.com/luafun/luafun/issues/20#issuecomment-207777263
local function dup(n, f, s, var)
    local dup_helper = function(i, var_1, ...)
        if var_1 ~= nil then
            return var_1, select(i, var_1, ...), ...
        end
    end

    if type(n) ~= "number" then
        n, f, s, var = 1, n, f, s
    end
    return function(s2, v)
        return dup_helper(n, f( s2, v ))
    end, s, var
end

function util.load_input(day)
    return fun.totable(dup(io.lines("./data/day" .. day .. "-input.txt")))
end

function util.stringsplit(str, delimiter)
    local res = {}
    for s in string.gmatch(str, '([^' .. delimiter .. ']+)') do
        table.insert(res, s)
    end
    return res
end

function util.stringtotable(str)
    local res = {}
    for i = 1, string.len(str) do
        res[i] = string.sub(str, i, i)
    end
    return res
end

function util.dedup(t)
    local hash = {}
    for _,v in ipairs(t) do
        hash[v] = true
    end
    local res = {}
    for k,_ in pairs(hash) do
        res[#res + 1] = k
    end
    return res
end

function util.pack_coord(coord)
    return (coord.x << 8) + coord.y
end

function util.unpack_coord(index)
    local y = index & 0x00ff
    local x = index >> 8
    return { x = x, y = y}
end

return util
