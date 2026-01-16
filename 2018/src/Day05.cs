using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day05
{
    [Fact]
    public async Task Part01()
    {
        var polymer = await Util.GetInputAsStrings("day05-input.txt").SingleAsync();

        polymer = GetReactingPolymer(polymer);

        Assert.Equal(11590, polymer.Length);
    }

    [Fact]
    public async Task Part02()
    {
        var polymer = await Util.GetInputAsStrings("day05-input.txt").SingleAsync();
        const string units = "abcdefghijklmnopqrstuvwxyz";

        var shortest = units
            .ToArray()
            .Where(u => polymer.ToLower().Contains(u))
            .Select(u =>
                GetReactingPolymer(
                    new string(polymer.Where(c => char.ToLower(c) != u).ToArray())
                ).Length
            )
            .Min();

        Assert.Equal(4504, shortest);
    }

    private static string GetReactingPolymer(string polymer)
    {
        HashSet<int> indexes;
        while ((indexes = GetDestroyIndexes(polymer)).Count > 0)
        {
            polymer = new string(
                polymer.Where((_, i) => !indexes.Contains(i) && !indexes.Contains(i - 1)).ToArray()
            );
        }

        return polymer;
    }

    private static HashSet<int> GetDestroyIndexes(string polymer)
    {
        var destroyIndexes = polymer
            .Remove(polymer.Length - 1)
            .Select((c, i) => (c, polymer[i + 1]))
            .Select((t, i) => Destroys(t) ? i : -1)
            .Where(i => i != -1)
            .ToHashSet();
        var sanitizedDestroyIndexes = destroyIndexes
            .Where(i => !destroyIndexes.Contains(i - 1))
            .ToHashSet();
        return sanitizedDestroyIndexes;
    }

    private static bool Destroys((char c, char) t) =>
        t.c.ToString().Equals(t.Item2.ToString(), StringComparison.CurrentCultureIgnoreCase)
        && (
            (char.IsLower(t.c) && char.IsUpper(t.Item2))
            || (char.IsUpper(t.c) && char.IsLower(t.Item2))
        );
}
