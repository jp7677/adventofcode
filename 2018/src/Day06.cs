using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day06
{
    record Coord(int X, int Y)
    {
        public int Distance(Coord other) => Math.Abs(X - other.X) + Math.Abs(Y - other.Y);
    }

    [Fact]
    public void Part01()
    {
        var coords = GetCoords();
        var (minX, minY, maxX, maxY) = GetMinMax(coords);
        var grid = BuildGrid(minY, maxY, minX, maxX);

        var maxArea = grid.Aggregate(
                new Dictionary<Coord, Coord>(),
                (areas, coord) =>
                {
                    var distances = coords
                        .Select(c => new Tuple<Coord, int>(c, coord.Distance(c)))
                        .ToList();

                    distances.Sort(
                        (a, b) =>
                        {
                            if (a.Item2 == b.Item2)
                                return 0;
                            if (a.Item2 > b.Item2)
                                return 1;
                            return -1;
                        }
                    );
                    if (distances[0].Item2 != distances[1].Item2)
                        areas[coord] = distances.First().Item1;

                    return areas;
                }
            )
            .GroupBy(pair => pair.Value)
            .Where(group =>
                group.All(pair =>
                    pair.Key.X != minX
                    && pair.Key.Y != minY
                    && pair.Key.X != maxX
                    && pair.Key.Y != maxY
                )
            )
            .MaxBy(grouping => grouping.Count())
            .Count();

        Assert.Equal(3687, maxArea);
    }

    [Fact]
    public void Part02()
    {
        var coords = GetCoords();
        var (minX, minY, maxX, maxY) = GetMinMax(coords);
        var grid = BuildGrid(minY, maxY, minX, maxX);

        var regionSize = grid.Aggregate(
                new Dictionary<Coord, int>(),
                (location, coord) =>
                {
                    location[coord] = coords
                        .Select(c => new Tuple<Coord, int>(c, coord.Distance(c)))
                        .Sum(t => t.Item2);

                    return location;
                }
            )
            .Count(location => location.Value < 10_000);

        Assert.Equal(40134, regionSize);
    }

    private static IEnumerable<Coord> BuildGrid(int minY, int maxY, int minX, int maxX) =>
        Enumerable
            .Range(minY, maxY - minY + 1)
            .SelectMany(y => Enumerable.Range(minX, maxX - minX + 1).Select(x => new Coord(x, y)));

    private static (int, int, int, int) GetMinMax(HashSet<Coord> coords)
    {
        var minX = coords.Select(coord => coord.X).Min();
        var minY = coords.Select(coord => coord.Y).Min();
        var maxX = coords.Select(coord => coord.X).Max();
        var maxY = coords.Select(coord => coord.Y).Max();
        return (minX, minY, maxX, maxY);
    }

    private static HashSet<Coord> GetCoords() =>
        Util.GetInputAsStrings("day06-input.txt")
            .ToBlockingEnumerable()
            .Select(line =>
            {
                var p = line.Split(", ");
                return new Coord(int.Parse(p[0]), int.Parse(p[1]));
            })
            .ToHashSet();
}
