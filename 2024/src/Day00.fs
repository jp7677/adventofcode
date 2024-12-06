namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day00 =

    [<Fact>]
    let ``part 01`` () =
        let result = input "day00-input.txt" |> Seq.head |> int

        result |> should equal 0
