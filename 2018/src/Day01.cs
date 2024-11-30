using System;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace aoc2018;

public class Day01
{
    [Fact]
    public void Part01()
    {
        var result = Util.GetInputAsStrings("day01-input.txt").ToList().Sum(Convert.ToInt32);

        Assert.Equal(500, result);
    }

    [Fact]
    public void Part02()
    {
        var frequencyChanges = Util.GetInputAsStrings("day01-input.txt")
            .ToList()
            .Select(f => Convert.ToInt32(f))
            .ToList();

        var frequencies = new HashSet<int>();
        var frequency = 0;
        while (true)
        {
            foreach (var frequencyChange in frequencyChanges)
            {
                frequency += frequencyChange;
                if (frequencies.Contains(frequency))
                    goto done;

                frequencies.Add(frequency);
            }
        }

        done:

        Assert.Equal(709, frequency);
    }
}
