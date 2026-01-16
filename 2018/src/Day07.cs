using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day07
{
    [Fact]
    public async Task Part01()
    {
        var requirements = await Util.GetInputAsStrings("day07-input.txt")
            .Select(line => new Tuple<char, char>(line[5], line[36]))
            .AggregateAsync(
                new Dictionary<char, HashSet<char>>(),
                (acc, step) =>
                {
                    if (acc.TryGetValue(step.Item1, out var next))
                        next.Add(step.Item2);
                    else
                        acc.Add(step.Item1, [step.Item2]);
                    return acc;
                }
            );

        var available = new SortedSet<char>(
            requirements.Keys.Where(step => !requirements.Values.Any(steps => steps.Contains(step)))
        );
        var order = new List<char>();

        while (available.Count > 0)
        {
            var next = available.Min;

            order.Add(next);
            available.Remove(next);

            if (requirements.TryGetValue(next, out var candidates))
                candidates
                    .Where(candidate =>
                    {
                        return requirements
                            .Where(s => s.Value.Contains(candidate))
                            .Select(s => s.Key)
                            .All(p => order.Contains(p));
                    })
                    .ToList()
                    .ForEach(candidate => available.Add(candidate));
        }

        Assert.Equal("CHILFNMORYKGAQXUVBZPSJWDET", string.Concat(order));
    }
}
