using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018;

public class Day08
{
    private record Header
    {
        public int ChildrenCount;
        public int MetadataCount;
    }

    private record Node
    {
        public readonly Header Header = new();
        public readonly List<Node> Children = [];
        public readonly List<int> Metadata = [];

        public int MetadataSum() =>
            Children.Select(c => c.MetadataSum()).Sum() + Metadata.Select(c => c).Sum();
    }

    [Fact]
    public async Task Part01()
    {
        var numbers = await Util.GetInputAsStrings("day08-input.txt")
            .Select(line => line.Split(' ').Select(int.Parse).ToArray())
            .SingleAsync();

        var position = -1;
        var tree = ParseNode(numbers, ref position);

        Assert.Equal(42196, tree.MetadataSum());
    }

    private static Node ParseNode(int[] numbers, ref int position)
    {
        var node = new Node();
        node.Header.ChildrenCount = numbers[++position];
        node.Header.MetadataCount = numbers[++position];
        foreach (var _ in Enumerable.Range(0, node.Header.ChildrenCount))
            node.Children.Add(ParseNode(numbers, ref position));
        foreach (var _ in Enumerable.Range(0, node.Header.MetadataCount))
            node.Metadata.Add(numbers[++position]);

        return node;
    }
}
