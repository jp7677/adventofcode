namespace aoc2024

open Xunit

module Day00 =

    [<Fact>]
    let ``Part 01`` () =
        let lines = Util.ReadInput("day00-input.txt")
        let first = lines |> Seq.head |> int
        Assert.Equal(0, first)
