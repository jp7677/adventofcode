local bstd = require "busted"
local fun = require "fun"
local util = require "util"

local function load_ingredients()
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

    return ranges, ids
end

local fn_day05_part1 = function()
    local ranges, ids = load_ingredients()

    local fresh = fun.iter(ids)
        :filter(function(x)
            return fun.iter(ranges):any(function(y) return x >= y[1] and x <= y[2] end)
        end)
        :length()

    bstd.assert.same(773, fresh)
end

local function union_ranges(a, b)
    if #a == 0 then return { b } end
    if #b == 0 then return { a } end

    if b[1] == a[2] + 1 then return { { a[1], b[2] } } end
    if b[1] > a[2] then return { a, b} end
    if b[2] == a[1] - 1 then return { { b[1], a[2]}  } end
    if b[2] < a[1] then return { b, a} end

    if b[1] >= a[1] and b[2] <= a[2] then return { a } end
    if a[1] >= b[1] and a[2] <= b[2] then return { b } end

    if b[1] < a[1] and b[2] <= a[2] then return { { b[1], a[2] } } end
    if a[1] < b[1] and a[2] <= b[2] then return { { a[1], b[2] } } end

    error()
end

bstd.it("unions #ranges", function()
    bstd.assert.same({ { 3, 4 } }, union_ranges({}, { 3, 4 }))
    bstd.assert.same({ { 3, 4 } }, union_ranges({ 3, 4 }, {}))

    bstd.assert.same({ { 1, 4 } }, union_ranges({ 1, 2 }, { 3, 4 }))
    bstd.assert.same({ { 1, 2 }, { 4, 5 } }, union_ranges({ 1, 2 }, { 4, 5 }))
    bstd.assert.same({ { 1, 4 } }, union_ranges({ 3, 4 }, { 1, 2 }))
    bstd.assert.same({ { 1, 2 }, { 4, 5 } }, union_ranges({ 4, 5 }, { 1, 2 }))

    bstd.assert.same({ { 1, 5 } }, union_ranges({ 1, 5 }, { 1, 2 }))
    bstd.assert.same({ { 1, 5 } }, union_ranges({ 1, 2 }, { 1, 5 }))

    bstd.assert.same({ { 1, 5 } }, union_ranges({ 2, 5 }, { 1, 5 }))
    bstd.assert.same({ { 1, 5 } }, union_ranges({ 1, 5 }, { 2, 5 }))
    bstd.assert.same({ { 2, 6 } }, union_ranges({ 2, 5 }, { 2, 6 }))
    bstd.assert.same({ { 2, 6 } }, union_ranges({ 2, 6 }, { 2, 5 }))
end)

local function intersect_ranges(a, b)
    if b[1] == a[2] + 1 then return {} end
    if b[1] > a[2] then return {} end
    if b[2] == a[1] - 1 then return {} end
    if b[2] < a[1] then return {} end

    if b[1] >= a[1] and b[2] <= a[2] then return b end
    if a[1] >= b[1] and a[2] <= b[2] then return a end

    if b[1] < a[1] and b[2] <= a[2] then return { a[1], b[2] } end
    if a[1] < b[1] and a[2] <= b[2] then return { b[1], a[2] } end

    error()
end

bstd.it("intersects #ranges", function()
    bstd.assert.same( {}, intersect_ranges({ 1, 2 }, { 3, 4 }))
    bstd.assert.same( {}, intersect_ranges({ 1, 2 }, { 4, 5 }))
    bstd.assert.same( {}, intersect_ranges({ 3, 4 }, { 1, 2 }))
    bstd.assert.same( {}, intersect_ranges({ 4, 5 }, { 1, 2 }))

    bstd.assert.same( { 1, 2 }, intersect_ranges({ 1, 5 }, { 1, 2 }))
    bstd.assert.same( { 1, 2 }, intersect_ranges({ 1, 2 }, { 1, 5 }))

    bstd.assert.same( { 2, 5 }, intersect_ranges({ 2, 5 }, { 1, 5 }))
    bstd.assert.same( { 2, 5 }, intersect_ranges({ 1, 5 }, { 2, 5 }))
    bstd.assert.same( { 2, 5 }, intersect_ranges({ 2, 5 }, { 2, 6 }))
    bstd.assert.same( { 2, 5 }, intersect_ranges({ 2, 6 }, { 2, 5 }))
end)

local function substract_ranges(a, b)
    if b[1] > a[2] then return { a } end
    if b[2] < a[1] then return { a } end

    if b[1] > a[1] and b[2] < a[2] then return { { a[1], b[1] }, { b[2] + 1, a[2] } } end
    if b[1] > a[1] and b[2] >= a[2] then return { { a[1], b[1] - 1 } } end
    if b[1] <= a[1] and b[2] < a[2] then return { { b[2] + 1, a[2] } } end

    return {}
end

bstd.it("substracts #ranges, range b from range a", function()
    bstd.assert.same( { { 1, 2 } }, substract_ranges({ 1, 2 }, { 3, 4 }))
    bstd.assert.same( { { 1, 2 } }, substract_ranges({ 1, 2 }, { 4, 5 }))
    bstd.assert.same( { { 3, 4 } }, substract_ranges({ 3, 4 }, { 1, 2 }))
    bstd.assert.same( { { 4, 5 } }, substract_ranges({ 4, 5 }, { 1, 2 }))

    bstd.assert.same( { { 1, 2 }, { 4, 5} }, substract_ranges({ 1, 5 }, { 2, 3 }))
    bstd.assert.same( {}, substract_ranges({ 1, 2 }, { 1, 5 }))
    bstd.assert.same( {}, substract_ranges({ 1, 2 }, { 1, 2 }))

    bstd.assert.same( {}, substract_ranges({ 2, 5 }, { 1, 5 }))
    bstd.assert.same( { { 1, 1 } }, substract_ranges({ 1, 5 }, { 2, 5 }))
    bstd.assert.same( { { 1, 2 } }, substract_ranges({ 1, 5 }, { 3, 5 }))
    bstd.assert.same( { { 1, 2 } }, substract_ranges({ 1, 5 }, { 3, 6 }))
    bstd.assert.same( {}, substract_ranges({ 2, 5 }, { 2, 6 }))
    bstd.assert.same( { { 6, 6 } }, substract_ranges({ 2, 6 }, { 2, 5 }))
    bstd.assert.same( { { 4, 6 } }, substract_ranges({ 2, 6 }, { 2, 3 }))
    bstd.assert.same( { { 4, 6 } }, substract_ranges({ 2, 6 }, { 1, 3 }))
end)


local fn_day05_part2 = function()
    local ranges, _ = load_ingredients()

    table.sort(ranges, function(a, b) return a[1] < b[1] end)

    local fresh_ranges = { ranges[1] }
    fun.iter(ranges)
        :drop(1)
        :each(function(range)
            local new_fresh_range = fun.iter(fresh_ranges)
                :foldl(function(acc, fresh_range)
                    if acc == nil then return acc end
                    -- because ranges are sorted, we never
                    -- get two ranges when substracting
                    return substract_ranges(acc, fresh_range)[1]
                end, range)
            if new_fresh_range ~= nil then table.insert(fresh_ranges, new_fresh_range) end
        end)

    local fresh_sum = fun.iter(fresh_ranges)
        :map(function(x) return x[2] - x[1] + 1 end)
        :sum()

    bstd.assert.same(332067203034711, fresh_sum)
end

bstd.it("solves #day05 part 1", fn_day05_part1)
bstd.it("solves #day05 part 2", fn_day05_part2)
