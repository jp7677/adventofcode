using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

internal record Claim(int Id, int X, int Y, int Width, int Height);

internal record Position(int X, int Y);

public class Day03
{
    private readonly Regex _regex = new("(\\d+)", RegexOptions.IgnoreCase);

    [Fact]
    public async Task Part01()
    {
        var claims = ReadClaims();
        var positions = await claims.SelectMany(claim => claim.Item2).ToListAsync();

        var squares = positions.GroupBy(p => p).Count(g => g.Count() > 1);

        Assert.Equal(109785, squares);
    }

    [Fact]
    public async Task Part02()
    {
        var claims = await ReadClaims().ToListAsync();
        var positions = claims.SelectMany(claim => claim.Item2);

        var intactSquares = positions
            .GroupBy(p => p)
            .Where(g => g.Count() == 1)
            .Select(g => g.Key)
            .ToHashSet();

        var intactClaimId = claims.Single(c => c.Item2.All(s => intactSquares.Contains(s))).Item1;

        Assert.Equal(504, intactClaimId);
    }

    private IAsyncEnumerable<(int, IEnumerable<Position>)> ReadClaims() =>
        Util.GetInputAsStrings("day03-input.txt")
            .Select(line => _regex.Matches(line).Select(m => int.Parse(m.Value)).ToList())
            .Select(n => new Claim(n[0], n[1], n[2], n[3], n[4]))
            .Select(claim =>
                (
                    claim.Id,
                    Enumerable
                        .Range(claim.X, claim.Width)
                        .SelectMany(x =>
                            Enumerable.Range(claim.Y, claim.Height).Select(y => new Position(x, y))
                        )
                )
            );
}
