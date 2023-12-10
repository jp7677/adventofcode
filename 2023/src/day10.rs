use crate::day10::Direction::{EAST, NORTH, SOUTH, WEST};
use crate::day10::Pipe::{Bend7, BendF, BendJ, BendL, PipeHorizontal, PipeVertical, Start};
use crate::util::*;
use std::collections::HashMap;

#[derive(Debug, Clone, Copy, PartialEq)]
enum Pipe {
    PipeVertical,
    PipeHorizontal,
    BendL,
    BendJ,
    Bend7,
    BendF,
    Start,
}

#[derive(Debug, PartialEq)]
enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
}

#[derive(Debug, Eq, Hash, PartialEq, Clone)]
struct Coord {
    x: u32,
    y: u32,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day10);

    let map = parse_map(&input);

    let start = map.iter().find(|(_, v)| *v == &Start).unwrap();
    let start = (start.0.clone(), *start.1);

    let current = start_neighbours(&start.0);
    let current = current
        .iter()
        .filter(|n| map.get(&n.0) == Some(&n.1))
        .collect::<Vec<_>>();
    let current = current.first().unwrap();
    let current = (current.0.clone(), current.1);

    let mut path = vec![start, current];
    loop {
        let last = path.iter().nth(path.len() - 2).unwrap();
        let current = path.last().unwrap();
        let current = neighbours(&current.0, current.1);
        let current = current
            .iter()
            .filter(|n| map.get(&n.0) == Some(&n.1) && n.0 != last.0)
            .collect::<Vec<_>>();

        match current.first() {
            None => break,
            Some(v) => path.push((v.0.clone(), v.1)),
        }
    }

    let max_distance = (path.len() + 1) / 2;

    assert_eq!(max_distance, 6717);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
}

fn parse_map(input: &String) -> HashMap<Coord, Pipe> {
    let mut map: HashMap<Coord, Pipe> = HashMap::new();
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol != '.' {
                let coord = Coord {
                    x: x as u32 + 1,
                    y: y as u32 + 1,
                };
                let pipe = match symbol {
                    '|' => PipeVertical,
                    '-' => PipeHorizontal,
                    'L' => BendL,
                    'J' => BendJ,
                    '7' => Bend7,
                    'F' => BendF,
                    'S' => Start,
                    _ => panic!("Unexpected symbol"),
                };
                map.insert(coord, pipe);
            }
        }
    }
    map
}

fn start_neighbours(coord: &Coord) -> [(Coord, Pipe); 12] {
    let mut v = Vec::new();
    v.extend(neighbours(coord, PipeHorizontal));
    v.extend(neighbours(coord, PipeVertical));
    v.try_into().unwrap()
}

fn neighbours(coord: &Coord, pipe: Pipe) -> [(Coord, Pipe); 6] {
    let mut v = Vec::new();
    for d in connecting_direction(&pipe) {
        let n = neighbour(&d, &coord);
        for p in connecting_pipes(&d) {
            v.push((n.clone(), p));
        }
    }
    v.try_into().unwrap()
}

fn connecting_direction(pipe: &Pipe) -> [Direction; 2] {
    match pipe {
        PipeVertical => [NORTH, SOUTH],
        PipeHorizontal => [EAST, WEST],
        BendL => [NORTH, EAST],
        BendJ => [NORTH, WEST],
        Bend7 => [SOUTH, WEST],
        BendF => [SOUTH, EAST],
        Start => panic!("Not expected to call"),
    }
}

fn connecting_pipes(direction: &Direction) -> [Pipe; 3] {
    match direction {
        NORTH => [PipeVertical, Bend7, BendF],
        SOUTH => [PipeVertical, BendL, BendJ],
        EAST => [PipeHorizontal, BendJ, Bend7],
        WEST => [PipeHorizontal, BendL, BendF],
    }
}

fn neighbour(out: &Direction, coord: &Coord) -> Coord {
    match out {
        NORTH => Coord {
            x: coord.x,
            y: coord.y - 1,
        },
        SOUTH => Coord {
            x: coord.x,
            y: coord.y + 1,
        },
        EAST => Coord {
            x: coord.x + 1,
            y: coord.y,
        },
        WEST => Coord {
            x: coord.x - 1,
            y: coord.y,
        },
    }
}
