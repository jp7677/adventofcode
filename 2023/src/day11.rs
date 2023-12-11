use crate::util::*;
use std::collections::HashSet;

#[derive(Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Coord {
    x: u64,
    y: u64,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day11);

    let map = parse_map(&input);
    let x_gaps = gaps(&map, |c| c.x);
    let y_gaps = gaps(&map, |c| c.y);
    let pairs = get_pairs(&map);

    let sum_of_sortest_path = pairs
        .iter()
        .fold(0, |acc, (a, b)| acc + distance(a, b, &x_gaps, &y_gaps, 2));

    assert_eq!(sum_of_sortest_path, 9724940);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day11);

    let map = parse_map(&input);
    let x_gaps = gaps(&map, |c| c.x);
    let y_gaps = gaps(&map, |c| c.y);
    let pairs = get_pairs(&map);

    let sum_of_sortest_path = pairs.iter().fold(0, |acc, (a, b)| {
        acc + distance(a, b, &x_gaps, &y_gaps, 1000000)
    });

    assert_eq!(sum_of_sortest_path, 569052586852);
}

fn distance(a: &Coord, b: &Coord, x_gaps: &HashSet<u64>, y_gaps: &HashSet<u64>, times: u64) -> u64 {
    let x_range = if a.x > b.x { b.x..a.x } else { a.x..b.x };
    let y_range = if a.y > b.y { b.y..a.y } else { a.y..b.y };

    let x_gap = x_gaps
        .iter()
        .fold(0, |acc, x| if x_range.contains(x) { acc + 1 } else { acc });
    let y_gap = y_gaps
        .iter()
        .fold(0, |acc, y| if y_range.contains(y) { acc + 1 } else { acc });

    abs(a.x, b.x) + ((x_gap * times) - x_gap) + abs(a.y, b.y) + ((y_gap * times) - y_gap)
}

fn gaps(map: &HashSet<Coord>, f: fn(coord: &Coord) -> u64) -> HashSet<u64> {
    let mut gaps = HashSet::new();
    let row = map.iter().map(|c| f(c)).collect::<HashSet<u64>>();
    for e in 0..map.iter().map(|c| f(c)).max().unwrap() {
        if !row.contains(&e) {
            gaps.insert(e);
        }
    }
    gaps
}

fn get_pairs(map: &HashSet<Coord>) -> HashSet<(&Coord, &Coord)> {
    let pairs: HashSet<(&Coord, &Coord)> = map
        .iter()
        .flat_map(|c1| {
            map.iter().filter_map(move |c2| {
                if c1 > c2 {
                    Some((c1, c2))
                } else if c1 < c2 {
                    Some((c2, c1))
                } else {
                    None
                }
            })
        })
        .collect::<HashSet<(&Coord, &Coord)>>();
    pairs
}

fn parse_map(input: &String) -> HashSet<Coord> {
    let mut map: HashSet<Coord> = HashSet::new();
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol != '.' {
                map.insert(Coord {
                    x: x as u64,
                    y: y as u64,
                });
            }
        }
    }
    map
}
