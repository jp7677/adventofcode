namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day02 =

    let readReports () =
        input "day02-input.txt"
        |> Seq.map _.Split(" ")
        |> Seq.map (fun p -> p |> Seq.map int |> Seq.toList)

    let mapToDeltas = Seq.map (fun report -> report |> List.pairwise |> List.map (fun (l1, l2) -> l2 - l1))

    let safeRules =
        fun deltas ->
            deltas |> List.forall (fun delta -> (delta >= 1 && delta <= 3))
            || deltas |> List.forall (fun delta -> (delta <= -1 && delta >= -3))

    [<Fact>]
    let ``part 01`` () =
        let safeReports = readReports () |> mapToDeltas |> Seq.filter safeRules |> Seq.toList

        safeReports |> should haveLength 483

    let safeWithOneBadRules: int list -> bool =
        fun report ->
            [ 0 .. report.Length - 1 ]
            |> Seq.map (fun b -> report |> List.removeAt b)
            |> mapToDeltas
            |> Seq.exists safeRules

    [<Fact>]
    let ``part 02`` () =
        let safeReports = readReports () |> Seq.filter safeWithOneBadRules |> Seq.toList

        safeReports |> should haveLength 528
