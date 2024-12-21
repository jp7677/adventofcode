using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day02
{
    [Fact]
    public void Part01()
    {
        var ids = Util.GetInputAsStrings("day02-input.txt")
            .ToBlockingEnumerable()
            .Select(s => s.ToCharArray().GroupBy(c => c).ToDictionary(g => g.Key, g => g.Count()))
            .ToList();

        var twice = ids.Count(x => x.ContainsValue(2));
        var three = ids.Count(x => x.ContainsValue(3));

        Assert.Equal(7134, twice * three);
    }

    [Fact]
    public void Part02()
    {
        var ids = Util.GetInputAsStrings("day02-input.txt").ToBlockingEnumerable().ToList();

        var prototypes = ids.Where(id1 =>
                ids.Count(id2 =>
                    id1.ToCharArray().Select((c, i) => (i, c)).Sum(t => t.c != id2[t.i] ? 1 : 0)
                    == 1
                ) == 1
            )
            .ToList();

        Assert.Equal(2, prototypes.Count);

        var common = prototypes
            .First()
            .ToCharArray()
            .Where((c, i) => c == prototypes.Last()[i])
            .ToArray();

        Assert.Equal("kbqwtcvzhmhpoelrnaxydifyb", new string(common));
    }
}
