namespace Aoc2024

open Xunit
open FsUnit.Xunit
open Util

module Day06 =

    [<Struct>]
    type Coord = { x: int; y: int }

    type Direction =
        | NORTH
        | EAST
        | SOUTH
        | WEST

    [<Struct>]
    type Guard = { coord: Coord; direction: Direction }

    let moveNorth state =
        { coord =
            { x = state.coord.x
              y = state.coord.y - 1 }
          direction = state.direction }

    let moveEast state =
        { coord =
            { x = state.coord.x + 1
              y = state.coord.y }
          direction = state.direction }

    let moveSouth state =
        { coord =
            { x = state.coord.x
              y = state.coord.y + 1 }
          direction = state.direction }

    let moveWest state =
        { coord =
            { x = state.coord.x - 1
              y = state.coord.y }
          direction = state.direction }

    let move state =
        match state.direction with
        | NORTH -> moveNorth state
        | EAST -> moveEast state
        | SOUTH -> moveSouth state
        | WEST -> moveWest state

    let turn state =
        let direction =
            match state.direction with
            | NORTH -> EAST
            | EAST -> SOUTH
            | SOUTH -> WEST
            | WEST -> NORTH

        { coord = { x = state.coord.x; y = state.coord.y }
          direction = direction }

    [<Fact>]
    let ``part 01`` () =
        let map = input "day06-input.txt" |> Seq.toList
        let max = map |> List.length |> Int.minus 1

        let guard =
            map
            |> Seq.mapi (fun y s ->
                match s |> Seq.tryFindIndex (fun c -> c = '^') with
                | Some x -> Some { x = x; y = y }
                | None -> None)
            |> Seq.find _.IsSome
            |> Option.get
            |> (fun c -> { coord = c; direction = NORTH })

        let obstructions =
            [ 0..max ]
            |> Seq.collect (fun x ->
                ([ 0..max ]
                 |> Seq.choose (fun y -> if map[y][x] = '#' then Some { x = x; y = y } else None)))
            |> Set.ofSeq

        let steps =
            guard
            |> Seq.unfold (fun it ->
                if it.coord.x < 0 || it.coord.x > max || it.coord.y < 0 || it.coord.y > max then
                    None
                else
                    let peek = move it

                    let next =
                        if (obstructions.Contains({ x = peek.coord.x; y = peek.coord.y })) then
                            turn it |> move
                        else
                            peek

                    Some(it, next))

        let distinctSteps = steps |> Seq.map _.coord |> Set.ofSeq |> Set.count

        distinctSteps |> should equal 5212
