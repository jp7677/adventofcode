namespace Aoc2024

open System
open FsUnit.Xunit
open Xunit
open Util

module Day01 =

    let readLists () =
        let lists =
            input "day01-input.txt"
            |> Seq.map _.Split("   ")
            |> Seq.map (fun p -> p[0] |> int, p[1] |> int)
            |> Seq.toList

        (lists |> List.map fst, lists |> List.map snd)

    [<Fact>]
    let ``part 01`` () =
        let list1, list2 = readLists ()
        let list2Sorted = list2 |> List.sort

        let distance =
            list1
            |> List.sort
            |> List.indexed
            |> List.sumBy (fun (index, id1) -> Math.Abs(id1 - list2Sorted[index]))

        distance |> should equal 2164381

    [<Fact>]
    let ``part 02`` () =
        let list1, list2 = readLists ()

        let score =
            list1
            |> List.sumBy (fun id1 -> list2 |> List.countBy (fun id2 -> id2 = id1) |> Int.times id1)

        score |> should equal 20719933
