namespace Aoc2024

open System
open System.Text.RegularExpressions

module Util =
    let input name =
        let path = $"{Reflection.Assembly.GetExecutingAssembly().Location}/../../../../data/{name}"
        seq { yield! IO.File.ReadLines path }

    let matchRegex pattern input =
        seq {
            for m in Regex.Matches(input, pattern) do
                yield m.Value
        }

module List =
    let inline countBy (predicate: 'T -> bool) list =
        list |> List.filter predicate |> List.length

module Int =
    let inline plus (b: int) int = int + b
    let inline minus (b: int) int = int - b
    let inline times (b: int) int = int * b
    let inline div (b: int) int = int / b
