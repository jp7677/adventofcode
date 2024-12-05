namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day04 =

    let matchXMAS = matchRegex "(XMAS)"
    let matchSAMX = matchRegex "(SAMX)"

    let countXmas path =
        (path |> Seq.map matchXMAS |> Seq.collect id |> Seq.length)
        + (path |> Seq.map matchSAMX |> Seq.collect id |> Seq.length)

    let mapDiagonal max (path : string list)  =
        [ 0..max ]
        |> Seq.collect (fun x -> (
            seq {
                yield
                    [ 0..max ]
                    |> Seq.choose (fun y -> if (x+y <= max) then Some (path[y][y+x]) else None)
                    |> Seq.toString
                yield
                    [ 0..max ]
                    |> Seq.choose (fun y -> if (x+y <= max) then Some (path[x+y][y]) else None)
                    |> Seq.toString
            }))
        |> Seq.tail // Skip duplicate

    [<Fact>]
    let ``part 01`` () =
        let path = input "day04-input.txt" |> Seq.toList
        let max = path |> List.length |> Int.minus 1

        let pathH = path
        let pathV =
            [ 0..max ]
            |> List.map (fun x -> ([ 0..max ] |> Seq.map (fun y -> path[y][x]) |> Seq.rev |> Seq.toString))

        let pathD1 = mapDiagonal max pathH
        let pathD2 = mapDiagonal max pathV

        let count = countXmas pathH + countXmas pathV + countXmas pathD1 + countXmas pathD2

        count |> should equal 2633

    [<Struct>]
    type Coord = { x: int; y: int }

    [<Fact>]
    let ``part 02`` () =
        let path = input "day04-input.txt" |> Seq.toList
        let max = path |> List.length |> Int.minus 1

        let chars =
            [ 0..max ]
            |> Seq.collect (fun x -> ([ 0..max ] |> Seq.map (fun y -> ({x=x;y=y},path[y][x]))))
            |> Map.ofSeq

        let count =chars |> Map.countBy (fun k v ->
            if k.x = 0 || k.x = max || k.y = 0 || k.y = max || v <> 'A' then
                false
            else
                let adj1 = chars |> Map.find {x=k.x-1;y=k.y-1}
                let adj2 = chars |> Map.find {x=k.x-1;y=k.y+1}
                let adj3 = chars |> Map.find {x=k.x+1;y=k.y+1}
                let adj4 = chars |> Map.find {x=k.x+1;y=k.y-1}

                let c1 = [adj1;adj3] |> List.sort
                let c2 = [adj2;adj4] |> List.sort

                c1 = ['M';'S'] && c2 = ['M';'S'])

        count |> should equal 1936
