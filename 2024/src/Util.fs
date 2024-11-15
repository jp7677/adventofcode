namespace aoc2024

open System

module Util =

    let input(name: string) =
        let path = $"{Reflection.Assembly.GetExecutingAssembly().Location}/../../../../data/{name}"
        seq { yield! IO.File.ReadLines path }