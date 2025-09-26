local util = {}

function util.load_input(day)
    local lines = {}
    for line in io.lines("./data/day" .. day .. "-input.txt") do
        lines[#lines + 1] = line
    end
    return lines
end

return util
