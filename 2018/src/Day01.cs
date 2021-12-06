using System;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace aoc2018
{
    public class Day01
    {
        [Fact]
        public async Task Part01()
        {
            var frequencyChanges = await Util.GetInputAsStrings("day01-input.txt");

            var result = frequencyChanges
                .Select(f => new Tuple<char, int>(f[0], Convert.ToInt32(f.Substring(1))))
                .Sum(f => f.Item1 == '+' ? f.Item2 : f.Item2 * -1);

            Assert.Equal(500, result);
        }
    }
}