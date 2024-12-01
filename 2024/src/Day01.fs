namespace Aoc2024

open System
open FsUnit.Xunit
open Xunit
open Util

module Day01 =

    [<Fact>]
    let ``part 01`` () =
        let lists =
            input "day01-input.txt"
            |> Seq.map _.Split("   ")
            |> Seq.map (fun it -> (it[0] |> int, it[1] |> int))
            |> Seq.toList

        let list1 = lists |> List.map fst
        let list2 = lists |> List.map snd

        let list1Sorted = list1 |> List.sort
        let list2Sorted = list2 |> List.sort

        let diff =
            list1Sorted
            |> List.indexed
            |> List.map (fun (idx, num1) -> Math.Abs(num1 - list2Sorted[idx]))
            |> List.sum

        diff |> should equal 2164381
