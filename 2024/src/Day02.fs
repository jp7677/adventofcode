namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day02 =

    let readList () =
        input "day02-input.txt"
        |> Seq.map _.Split(" ")
        |> Seq.map (fun p -> p |> Array.toList |> List.map (fun s -> s |> int))
        |> Seq.toList

    [<Fact>]
    let ``part 01`` () =
        let safeReports =
            readList ()
            |> Seq.map (fun report -> report |> List.pairwise |> List.map (fun (l1, l2) -> l2 - l1))
            |> Seq.filter (fun deltas ->
                deltas |> List.forall (fun delta -> (delta >= 1 && delta <= 3))
                || deltas |> List.forall (fun delta -> (delta <= -1 && delta >= -3)))
            |> Seq.toList

        safeReports |> should haveLength 483
