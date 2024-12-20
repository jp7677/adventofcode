namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day07 =

    let opAdd (a: int64, b: int64) = a + b
    let opMul (a: int64, b: int64) = a * b
    let opConcat (a: int64, b: int64) = $"{a}{b}" |> int64

    let rec evaluate (operands, acc, index, maxIndex, maxResult, ops) =
        if index = maxIndex then
            seq [ acc ]
        else if acc > maxResult then
            seq [ 0L ]
        else
            let operand = operands |> Seq.item index

            ops
            |> Seq.collect (fun op -> evaluate (operands, op (acc, operand), index + 1, maxIndex, maxResult, ops))

    let calculateCalibrationResult ops =
        input "day07-input.txt"
        |> Seq.map _.Split(':')
        |> Seq.map (fun p -> p[0] |> int64, (p[1].Trim().Split(' ') |> Seq.map int64))
        |> Seq.filter (fun equation ->
            let result = fst equation
            let operands = snd equation
            let operand = operands |> Seq.item 0
            let maxLength = operands |> Seq.length

            evaluate (operands, operand, 1, maxLength, result, ops) |> Seq.contains result)
        |> Seq.map fst
        |> Seq.sum

    [<Fact>]
    let ``part 01`` () =
        let calibrationResult = calculateCalibrationResult [ opAdd; opMul ]

        calibrationResult |> should equal 1153997401072L

    [<Fact>]
    let ``part 02`` () =
        let calibrationResult = calculateCalibrationResult [ opAdd; opMul; opConcat ]

        calibrationResult |> should equal 97902809384118L
