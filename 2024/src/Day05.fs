namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day05 =

    let readManual () =
        let puzzle = input "day05-input.txt"
        let sepIndex = puzzle |> Seq.findIndex (fun s -> s = "")
        let rules = puzzle |> Seq.take sepIndex |> Seq.map _.Split("|") |> Seq.map (fun p -> p[0] |> int, p[1] |> int) |> Set.ofSeq
        let pages = puzzle |> Seq.skip (sepIndex + 1) |> Seq.map _.Split(",") |> Seq.map (fun s -> s |> Seq.map int) |> Seq.toList
        (rules, pages)

    let detectInvalidPages (rules: Set<'a>) pages =
            pages
            |> Seq.choose (fun p ->
                let length = p |> Seq.length |> Int.minus 1

                let pairs =
                    [0 .. length - 1]
                    |> List.collect (fun i ->
                        [i + 1 .. length]
                        |> List.map (fun c -> (p |> Seq.item i, p |> Seq.item c)))

                let valid =
                    pairs
                    |> List.countBy (fun pair -> rules.Contains((pair |> snd, pair |> fst)))
                    |> (fun count -> count = 0)

                if not valid then Some p else None)

    [<Fact>]
    let ``part 01`` () =
        let rules, pages = readManual()
        let invalidPages = detectInvalidPages rules pages

        let sum =
            pages
            |> Seq.except invalidPages
            |> Seq.sumBy (fun p -> p |> Seq.item ((p |> Seq.length) / 2))

        sum |> should equal 6951

    [<Fact>]
    let ``part 02`` () =
        let rules, pages = readManual()
        let invalidPages = detectInvalidPages rules pages

        let sum =
            invalidPages
            |> Seq.map (fun p ->
                rules
                    |> Seq.filter (fun rule -> (p |> Seq.contains (rule |> fst)) && (p |> Seq.contains (rule |> snd))))
            |> Seq.sumBy (fun rules ->
                let groups =
                    rules
                    |> Seq.map fst
                    |> Seq.groupBy id
                    |> Seq.sortByDescending (fun (_,g) -> g |> Seq.length)

                groups |> Seq.map fst |> Seq.item ((groups |> Seq.length) / 2))

        sum |> should equal 4121
