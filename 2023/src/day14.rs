use crate::util::*;
use std::cmp::max;
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
    let max_y = map.iter().map(|c| c.y).max().unwrap() + 1;
    let rocks = parse_map(&input, 'O');
    let rocks = tilt_north(&map, max_y, &rocks);

    let load = rocks.iter().map(|c| max_y - c.y).sum::<u32>();

    assert_eq!(load, 110821);
}

fn tilt_north(map: &HashSet<Coord>, max_y: u32, rocks: &HashSet<Coord>) -> HashSet<Coord> {
    let mut tilted: HashSet<Coord> = HashSet::new();

    (0..max_y).for_each(|i| {
        rocks.iter().filter(|r| r.y == i).for_each(|r| {
            let next_rock = next(map, r);
            let next_tilted = next(&tilted, r);
            tilted.insert(Coord {
                x: r.x,
                y: (max(next_tilted, next_rock) + 1) as u32,
            });
        })
    });
    tilted
}

fn next(map: &HashSet<Coord>, r: &Coord) -> i32 {
    map.iter()
        .filter(|m| m.x == r.x && m.y < r.y)
        .map(|m| m.y as i32)
        .max()
        .or(Some(-1))
        .unwrap()
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
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
