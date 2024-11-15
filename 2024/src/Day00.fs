namespace aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day00 =

    [<Fact>]
    let ``part 01``() =
        input "day00-input.txt" |> Seq.head |> int |> should equal 0
