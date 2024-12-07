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

    let getMap =
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

        max, obstructions, guard

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

    let getSteps max (obstructions: Set<Coord>) guard =
        guard
        |> Seq.unfold (fun it ->
            if it.coord.x < 0 || it.coord.x > max || it.coord.y < 0 || it.coord.y > max then
                None
            else
                let peek = move it

                let next =
                    if obstructions.Contains({ x = peek.coord.x; y = peek.coord.y }) then
                        turn it
                    else
                        peek

                Some(it, next))

    [<Fact>]
    let ``part 01`` () =
        let max, obstructions, guard = getMap
        let steps = getSteps max obstructions guard |> Seq.map _.coord |> Set.ofSeq
        let distinctSteps = steps |> Set.count

        // [ 0..max ]
        // |> List.iter (fun y1 ->
        //     printf " "
        //
        //     [ 0..max ]
        //     |> List.iter (fun x1 ->
        //         if obstructions.Contains { x = x1; y = y1 } then printf "#"
        //         else if steps.Contains { x = x1; y = y1 } then printf "X"
        //         else if guard.coord = { x = x1; y = y1 } then printf "^"
        //         else printf ".")
        //
        //     printfn "")
        //
        // printfn "_"

        distinctSteps |> should equal 5212

    [<Fact(Skip = "Slow")>]
    let ``part 02`` () =
        let max, obstructions, guard = getMap

        let steps = seq { guard } |> Seq.append (getSteps max obstructions guard)

        let possibleObstructions =
            steps
            |> Seq.map (fun it ->
                let newObstruction = it |> move |> _.coord

                if obstructions.Contains(newObstruction) || newObstruction = guard.coord then
                    None
                else
                    let alternativeObstructions = obstructions |> Set.add newObstruction
                    let alternativeSteps = getSteps max alternativeObstructions guard

                    let mutable alternativeGuardStepsSet: Set<Guard> = Set.empty

                    let findRepeat =
                        alternativeSteps
                        |> Seq.tryFind (fun alternativeStep ->
                            let foundRepeatedStep = alternativeGuardStepsSet.Contains(alternativeStep)
                            alternativeGuardStepsSet <- alternativeGuardStepsSet |> Set.add alternativeStep
                            foundRepeatedStep)

                    match findRepeat with
                    | None -> None
                    | Some _ -> Some newObstruction)
            |> Seq.choose id
            |> Set.ofSeq

        // [ 0..max ]
        // |> List.iter (fun y1 ->
        //     printf " "
        //
        //     [ 0..max ]
        //     |> List.iter (fun x1 ->
        //         if possibleObstructions.Contains { x = x1; y = y1 } then
        //             printf "O"
        //         else if obstructions.Contains { x = x1; y = y1 } then
        //             printf "#"
        //         else if guard.coord = { x = x1; y = y1 } then
        //             printf "^"
        //         else
        //             printf ".")
        //
        //     printfn "")
        //
        // printfn "_"

        possibleObstructions |> Set.count |> should equal 1767
