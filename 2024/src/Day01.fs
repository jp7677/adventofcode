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
            |> Seq.map (fun it -> it[0] |> int, it[1] |> int)
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
            |> List.sumBy (fun (index, it) -> Math.Abs(it - list2Sorted[index]))

        distance |> should equal 2164381

    [<Fact>]
    let ``part 02`` () =
        let list1, list2 = readLists ()

        let score =
            list1
            |> List.sumBy (fun id ->
                let c = list2 |> List.filter (fun it -> it = id) |> List.length
                id * c)

        score |> should equal 20719933
