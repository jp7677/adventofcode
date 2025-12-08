local set = {}

function set.add(s, key)
    s[key] = true
end

function set.remove(s, key)
    s[key] = nil
end

function set.contains(s, key)
    return s[key] ~= nil
end

function set.length(s)
  local count = 0
  for _, v in pairs(s) do
    if v ~= nil then count = count + 1 end
  end
  return count
end

function set.values(s)
  local copy = {}
  for i, v in pairs(s) do
    if v ~= nil then copy[#copy + 1] = i end
  end
  return copy
end

return set
