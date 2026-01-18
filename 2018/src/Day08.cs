using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day08
{
    private record Node
    {
        public readonly List<Node> Children = [];
        public readonly List<int> Metadata = [];

        public int MetadataSum() =>
            Children.Select(c => c.MetadataSum()).Sum() + Metadata.Select(m => m).Sum();

        public int Value() =>
            Children.Count == 0
                ? Metadata.Select(m => m).Sum()
                : Metadata.Select(m => m > Children.Count ? 0 : Children[m - 1].Value()).Sum();
    }

    [Fact]
    public async Task Part01()
    {
        var numbers = await GetNumbers();
        var tree = GetTree(numbers);

        Assert.Equal(42196, tree.MetadataSum());
    }

    [Fact]
    public async Task Part02()
    {
        var numbers = await GetNumbers();
        var tree = GetTree(numbers);

        Assert.Equal(33649, tree.Value());
    }

    private static Node GetTree(int[] numbers)
    {
        var idx = -1;
        return ParseNode(numbers, ref idx);
    }

    private static Node ParseNode(int[] numbers, ref int idx)
    {
        var childrenCount = numbers[++idx];
        var metadataCount = numbers[++idx];

        var node = new Node();
        foreach (var _ in Enumerable.Range(0, childrenCount))
            node.Children.Add(ParseNode(numbers, ref idx));
        foreach (var _ in Enumerable.Range(0, metadataCount))
            node.Metadata.Add(numbers[++idx]);

        return node;
    }

    private static ValueTask<int[]> GetNumbers() =>
        Util.GetInputAsStrings("day08-input.txt")
            .Select(line => line.Split(' ').Select(int.Parse).ToArray())
            .SingleAsync();
}
