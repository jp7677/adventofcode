using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Threading.Tasks;

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

    public static List<T> ToList<T>(
        this IAsyncEnumerable<T> items,
        CancellationToken cancellationToken = default
    )
    {
        return items.ToListAsync(cancellationToken).Result;
    }

    public static async Task<List<T>> ToListAsync<T>(
        this IAsyncEnumerable<T> items,
        CancellationToken cancellationToken = default
    )
    {
        var results = new List<T>();
        await foreach (var item in items.WithCancellation(cancellationToken).ConfigureAwait(false))
            results.Add(item);
        return results;
    }
}
