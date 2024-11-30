using System;
using System.Collections.Generic;
using System.IO;

namespace aoc2018;

public static class Util
{
    public static async IAsyncEnumerable<string> GetInputAsStrings(string name)
    {
        var stream = typeof(Util).Assembly.GetManifestResourceStream($"aoc2018.data.{name}");
        if (stream == null)
            throw new ArgumentOutOfRangeException(nameof(name));

        var reader = new StreamReader(stream);
        while (await reader.ReadLineAsync() is { } line)
        {
            yield return line;
        }
    }
}
