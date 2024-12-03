namespace Aoc2024

open FsUnit.Xunit
open Xunit
open Util

module Day03 =

    type Instr =
        | Do
        | Dont
        | Mul of int * int

    let readInstructions =
        input "day03-input.txt"
        |> Seq.reduce (fun a b -> a + b)
        |> matchRegex "(mul\(\d+,\d+\))|(do\(\))|(don't\(\))"
        |> Seq.map (fun m ->
            if m.StartsWith("mul") then
                let i = m |> matchRegex "(\d+)" |> Seq.toList
                Mul(i[0] |> int, i[1] |> int)
            else if m.StartsWith("don't") then
                Dont
            else
                Do)

    [<Fact>]
    let ``part 01`` () =
        let result =
            readInstructions
            |> Seq.fold
                (fun acc instr ->
                    match instr with
                    | Mul(a, b) -> acc + (a * b)
                    | _ -> acc)
                0

        result |> should equal 174960292

    type Status =
        | Enabled
        | Disabled

    [<Fact>]
    let ``part 02`` () =
        let result, _ =
            readInstructions
            |> Seq.fold
                (fun (acc, status) instr ->
                    match instr with
                    | Mul(a, b) -> (acc + (if status = Enabled then a * b else 0), status)
                    | Do -> (acc, Enabled)
                    | Dont -> (acc, Disabled))
                (0, Enabled)

        result |> should equal 56275602
