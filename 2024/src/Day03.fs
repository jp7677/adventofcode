namespace Aoc2024

open System.Text.RegularExpressions
open FsUnit.Xunit
open Xunit
open Util

module Day03 =

    type Instr =
        | Do
        | Dont
        | Mul of int * int

    type Status =
        | Enabled
        | Disabled

    let matchNumbers instruction =
        seq {
            for m in Regex.Matches(instruction, "(\d+)") do
                yield m.Value
        }

    let matchInstructions memory =
        seq {
            for m in Regex.Matches(memory, "(mul\(\d+,\d+\))|(do\(\))|(don't\(\))") do
                if m.Value.StartsWith("mul") then
                    let i = m.Value |> matchNumbers |> Seq.toList
                    yield Mul(i[0] |> int, i[1] |> int)
                else if m.Value.StartsWith("don't") then
                    yield Dont
                else
                    yield Do
        }

    let readInstructions = input "day03-input.txt" |> Seq.reduce (fun a b -> a + b) |> matchInstructions

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
