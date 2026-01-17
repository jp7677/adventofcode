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
        var requirements = await GetRequirements();
        var available = new SortedSet<char>(GetSartingSteps(requirements));
        var order = new List<char>();

        while (available.Count > 0)
        {
            var next = available.Min;

            order.Add(next);
            available.Remove(next);
            GetAvailableSteps(requirements, order, next)
                .ForEach(candidate => available.Add(candidate));
        }

        Assert.Equal("CHILFNMORYKGAQXUVBZPSJWDET", string.Concat(order));
    }

    private const int WorkerCount = 5;
    private const int TimeOffset = 60;

    private record Worker(char Step, int Remaining)
    {
        public static Worker New(char step) => new(step, StepTime(step, TimeOffset));

        public Worker Work() => this with { Remaining = Remaining - 1 };

        private static int StepTime(char step, int offset) => step - 'A' + offset;
    }

    [Fact]
    public async Task Part02()
    {
        var requirements = await GetRequirements();
        var workers = new Worker[WorkerCount];

        var starters = GetSartingSteps(requirements).ToList();
        for (var i = 0; i < starters.Count; i++)
            workers[i] = Worker.New(starters[i]);

        var done = new List<char>();
        var available = new SortedSet<char>();
        var seconds = -1;
        while (workers.Any(worker => worker != null))
        {
            seconds++;

            for (var i = 0; i < WorkerCount; i++)
            {
                if (workers[i] == null)
                    continue;

                if (workers[i].Remaining == 0)
                {
                    var finished = workers[i].Step;
                    workers[i] = null;
                    done.Add(finished);
                    GetAvailableSteps(requirements, done, finished)
                        .ForEach(candidate => available.Add(candidate));
                }
                else
                    workers[i] = workers[i].Work();
            }

            for (var i = 0; i < WorkerCount; i++)
            {
                if (workers[i] != null || available.Count <= 0)
                    continue;
                var next = available.Min;
                available.Remove(next);
                workers[i] = Worker.New(next);
            }
        }

        Assert.Equal(891, seconds + 1);
    }

    private static IEnumerable<char> GetSartingSteps(
        IReadOnlyDictionary<char, HashSet<char>> requirements
    ) => requirements.Keys.Where(step => !requirements.Values.Any(steps => steps.Contains(step)));

    private static List<char> GetAvailableSteps(
        IReadOnlyDictionary<char, HashSet<char>> requirements,
        IReadOnlyList<char> order,
        char next
    )
    {
        if (requirements.TryGetValue(next, out var candidates))
            return candidates
                .Where(candidate =>
                {
                    return requirements
                        .Where(s => s.Value.Contains(candidate))
                        .Select(s => s.Key)
                        .All(order.Contains);
                })
                .ToList();

        return [];
    }

    private static ValueTask<Dictionary<char, HashSet<char>>> GetRequirements() =>
        Util.GetInputAsStrings("day07-input.txt")
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
}
