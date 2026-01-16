using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using Xunit;

namespace aoc2018;

public class Day04
{
    private readonly Regex _regex = new("(\\d+)", RegexOptions.IgnoreCase);

    private record GuardRecords(int Id, List<int> Records);

    [Fact]
    public void Part01()
    {
        var records = ReadGuardRecords();

        var guardId = records
            .GroupBy(g => g.Id)
            .Select(g => (g.Key, g.SelectMany(r => r.Records).Count()))
            .OrderByDescending(r => r.Item2)
            .First()
            .Key;

        var minute = records
            .Where(g => g.Id == guardId)
            .GroupBy(g => g.Id)
            .Select(g =>
                (
                    g.Key,
                    g.SelectMany(r => r.Records)
                        .GroupBy(m => m)
                        .Select(r => (r.Key, r.Count()))
                        .OrderByDescending(r => r.Item2)
                )
            )
            .Single()
            .Item2.First()
            .Key;

        Assert.Equal(102688, guardId * minute);
    }

    [Fact]
    public void Part02()
    {
        var records = ReadGuardRecords();

        var (guardId, recordsByCount) = records
            .GroupBy(g => g.Id)
            .Select(g =>
                (
                    g.Key,
                    g.SelectMany(r => r.Records)
                        .GroupBy(m => m)
                        .Select(r => (r.Key, r.Count()))
                        .OrderByDescending(r => r.Item2)
                )
            )
            .Where(r => r.Item2.Any())
            .OrderByDescending(r => r.Item2.First().Item2)
            .First();

        var minute = recordsByCount.First().Key;

        Assert.Equal(56901, guardId * minute);
    }

    private List<GuardRecords> ReadGuardRecords() =>
        Util.GetInputAsStrings("day04-input.txt")
            .Select(line =>
            {
                var m = _regex.Matches(line).Select(m => int.Parse(m.Value)).ToList();
                var date = new DateTime(m[0], m[1], m[2], m[3], m[4], 0);
                return (date, line[19..]);
            })
            .OrderBy(r => r.date.Ticks)
            .AggregateAsync(
                new List<GuardRecords>(),
                (acc, it) =>
                {
                    var id = _regex.Matches(it.Item2).Select(m => int.Parse(m.Value)).ToList();
                    if (acc.Count == 0)
                        return [new GuardRecords(id.Single(), [])];

                    if (id.Count != 0)
                        acc.Add(new GuardRecords(id.Single(), []));
                    else
                        acc.Last().Records.Add(it.date.Minute);

                    return acc;
                }
            )
            .Result.Select(g =>
                g with
                {
                    Records = g
                        .Records.Chunk(2)
                        .SelectMany(x =>
                            Enumerable.Range(x.First(), x.Last() - x.First()).Select(i => i)
                        )
                        .ToList(),
                }
            )
            .ToList();
}
