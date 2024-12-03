namespace Aoc2024

open System.Text.RegularExpressions
open FsUnit.Xunit
open Xunit
open Util

module Day03 =

    let matchMul memory =
        seq {
            for m in Regex.Matches(memory, "(mul\(\d+,\d+\))") do
                yield m.Value
        }

    let matchNumbers memory =
        seq {
            for m in Regex.Matches(memory, "(\d+)") do
                yield m.Value
        }

    [<Fact>]
    let ``part 01`` () =
        let result =
            input "day03-input.txt"
            |> Seq.map matchMul
            |> Seq.map (fun line ->
                line
                |> Seq.map (fun instr -> instr |> matchNumbers |> Seq.map int |> Seq.reduce (fun a b -> a * b))
                |> Seq.reduce (fun a b -> a + b))
            |> Seq.reduce (fun a b -> a + b)

        result |> should equal 174960292
