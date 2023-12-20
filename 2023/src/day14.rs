use crate::util::*;
use std::cmp::{max, min};
use std::collections::HashSet;

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Coord {
    x: u32,
    y: u32,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day14);

    let map = parse_map(&input, '#');
    let rocks = parse_map(&input, 'O');

    let max_y = map.iter().map(|c| c.y).max().unwrap() + 1;
    let rocks = tilt_north(&map, max_y, &rocks);
    let load = rocks.iter().map(|c| max_y - c.y).sum::<u32>();

    assert_eq!(load, 110821);
}

fn tilt_north(map: &HashSet<Coord>, max_y: u32, rocks: &HashSet<Coord>) -> HashSet<Coord> {
    let mut tilted: HashSet<Coord> = HashSet::new();

    (0..max_y).for_each(|i| {
        rocks.iter().filter(|r| r.y == i).for_each(|r| {
            let next_rock = next_north(map, r);
            let next_tilted = next_north(&tilted, r);
            tilted.insert(Coord {
                x: r.x,
                y: (max(next_tilted, next_rock) + 1) as u32,
            });
        })
    });
    tilted
}

#[test]
#[ignore = "manually calculated after observing the load pattern"]
fn part02() {
    let input = read_input(DAYS::Day14);

    let map = parse_map(&input, '#');
    let mut rocks = parse_map(&input, 'O');

    let max_x = map.iter().map(|c| c.x).max().unwrap() + 1;
    let max_y = map.iter().map(|c| c.y).max().unwrap() + 1;
    (0..10000000).for_each(|_| {
        rocks = tilt_cycle(&map, max_x, max_y, rocks.clone());
    });
    let load = rocks.iter().map(|c| max_y - c.y).sum::<u32>();

    assert_eq!(load, 83516);
}

fn tilt_cycle(
    map: &HashSet<Coord>,
    max_x: u32,
    max_y: u32,
    mut rocks: HashSet<Coord>,
) -> HashSet<Coord> {
    let mut tilted = HashSet::new();
    (0..max_y).for_each(|i| {
        rocks.iter().filter(|r| r.y == i).for_each(|r| {
            let next_rock = next_north(map, r);
            let next_tilted = next_north(&tilted, r);
            tilted.insert(Coord {
                x: r.x,
                y: (max(next_tilted, next_rock) + 1) as u32,
            });
        })
    });

    rocks = HashSet::from(tilted);
    let mut tilted = HashSet::new();
    (0..max_x).for_each(|i| {
        rocks.iter().filter(|r| r.x == i).for_each(|r| {
            let next_rock = next_west(map, r);
            let next_tilted = next_west(&tilted, r);
            tilted.insert(Coord {
                x: (max(next_tilted, next_rock) + 1) as u32,
                y: r.y,
            });
        })
    });

    rocks = HashSet::from(tilted);
    let mut tilted = HashSet::new();
    (0..max_y).rev().for_each(|i| {
        rocks.iter().filter(|r| r.y == i).for_each(|r| {
            let next_rock = next_south(map, r, max_y);
            let next_tilted = next_south(&tilted, r, max_y);
            tilted.insert(Coord {
                x: r.x,
                y: (min(next_tilted, next_rock) - 1) as u32,
            });
        })
    });

    rocks = HashSet::from(tilted);
    let mut tilted = HashSet::new();
    (0..max_x).rev().for_each(|i| {
        rocks.iter().filter(|r| r.x == i).for_each(|r| {
            let next_rock = next_east(map, r, max_x);
            let next_tilted = next_east(&tilted, r, max_x);
            tilted.insert(Coord {
                x: (min(next_tilted, next_rock) - 1) as u32,
                y: r.y,
            });
        })
    });
    tilted
}

fn next_north(map: &HashSet<Coord>, r: &Coord) -> i32 {
    map.iter()
        .filter(|m| m.x == r.x && m.y < r.y)
        .map(|m| m.y as i32)
        .max()
        .or(Some(-1))
        .unwrap()
}

fn next_west(map: &HashSet<Coord>, r: &Coord) -> i32 {
    map.iter()
        .filter(|m| m.y == r.y && m.x < r.x)
        .map(|m| m.x as i32)
        .max()
        .or(Some(-1))
        .unwrap()
}

fn next_south(map: &HashSet<Coord>, r: &Coord, max_y: u32) -> i32 {
    map.iter()
        .filter(|m| m.x == r.x && m.y > r.y)
        .map(|m| m.y as i32)
        .min()
        .or(Some(max_y as i32))
        .unwrap()
}

fn next_east(map: &HashSet<Coord>, r: &Coord, max_x: u32) -> i32 {
    map.iter()
        .filter(|m| m.y == r.y && m.x > r.x)
        .map(|m| m.x as i32)
        .min()
        .or(Some(max_x as i32))
        .unwrap()
}

fn parse_map(input: &String, rock: char) -> HashSet<Coord> {
    let mut map: HashSet<Coord> = HashSet::new();
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol == rock {
                map.insert(Coord {
                    x: x as u32,
                    y: y as u32,
                });
            }
        }
    }
    map
}

fn debug_map(map: &HashSet<Coord>, rocks: &HashSet<Coord>, max_x: u32, max_y: u32) {
    (0..max_y).for_each(|y| {
        (0..max_x).for_each(|x| {
            if rocks.contains(&Coord { x, y }) {
                print!("O")
            } else if map.contains(&Coord { x, y }) {
                print!("#")
            } else {
                print!(".")
            }
        });
        println!();
    });
    println!();
}
