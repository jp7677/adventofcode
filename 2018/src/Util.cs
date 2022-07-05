using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

namespace aoc2018
{
    public static class Util
    {
        public static async Task<IEnumerable<string>> GetInputAsStrings(string name)
        {
            var stream = typeof(Util).Assembly.GetManifestResourceStream($"aoc2018.data.{name}");
            if (stream == null)
                throw new ArgumentOutOfRangeException(nameof(name));

            var reader = new StreamReader(stream);
            var content = await reader.ReadToEndAsync();
            return content.Trim().Split(Environment.NewLine);
        }
    }
}
