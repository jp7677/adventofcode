use crate::util::*;
use std::collections::{HashMap, HashSet};

#[derive(Eq, Hash, PartialEq, Clone)]
struct Coord {
    x: i32,
    y: i32,
}

#[derive(Eq, Hash, PartialEq, Clone)]
struct Adjacent {
    coord: Coord,
    symbol: char,
}

struct Part {
    number: String,
    adjacents: HashSet<Adjacent>,
}

impl Part {
    fn number(&self) -> u32 {
        self.number.parse::<u32>().unwrap()
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day03);

    let parts = parse_map(&input);
    let sum_of_parts = parts
        .iter()
        .filter(|p| p.adjacents.len() > 0)
        .fold(0, |acc, p| acc + p.number());

    assert_eq!(sum_of_parts, 528799);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day03);
    let parts = parse_map(&input);

    let mut gears = parts
        .iter()
        .filter_map(|p| p.adjacents.iter().find(|a| a.symbol == '*'))
        .map(|a| (a.coord.x, a.coord.y))
        .collect::<Vec<(i32, i32)>>();

    gears.sort();
    gears.dedup();

    let sum_of_gear_parts = gears.iter().fold(0, |acc, g| {
        let gear_parts = parts
            .iter()
            .filter(|p| {
                p.adjacents
                    .iter()
                    .any(|a| a.coord.x == g.0 && a.coord.y == g.1)
            })
            .collect::<Vec<&Part>>();
        if gear_parts.len() == 2 {
            acc + gear_parts.first().unwrap().number() * gear_parts.last().unwrap().number()
        } else {
            acc
        }
    });

    assert_eq!(sum_of_gear_parts, 84907174);
}

fn parse_map(input: &String) -> Vec<Part> {
    let mut map: HashMap<Coord, char> = HashMap::new();
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol != '.' {
                let coord = Coord {
                    x: x as i32,
                    y: y as i32,
                };
                map.insert(coord, symbol);
            }
        }
    }

    let map_size = input.lines().count() as i32;
    let mut parts: Vec<Part> = Vec::new();
    let mut last_char = None;
    for (i, c) in input.chars().enumerate() {
        if c.is_digit(10) {
            let x = i as i32 % (map_size + 1);
            let y = i as i32 / (map_size + 1);

            let adjacents = adjacent(x, y)
                .iter()
                .filter_map(|(a_x, a_y)| {
                    let coord = Coord { x: *a_x, y: *a_y };
                    let adjacent_coord = map.get(&coord);
                    match adjacent_coord {
                        None => None,
                        Some(v) => {
                            if !v.is_digit(10) {
                                Some(Adjacent { coord, symbol: *v })
                            } else {
                                None
                            }
                        }
                    }
                })
                .collect::<Vec<Adjacent>>();

            if last_char.unwrap_or('_').is_digit(10) {
                parts.last_mut().unwrap().number.push(c);
                parts
                    .last_mut()
                    .unwrap()
                    .adjacents
                    .extend(adjacents.iter().cloned());
            } else {
                parts.push(Part {
                    number: c.to_string(),
                    adjacents: HashSet::from_iter(adjacents.iter().cloned()),
                });
            }
        }
        last_char = Some(c);
    }
    parts
}

fn adjacent(x: i32, y: i32) -> [(i32, i32); 8] {
    [
        (x - 1, y - 1),
        (x, y - 1),
        (x + 1, y - 1),
        (x - 1, y),
        (x + 1, y),
        (x - 1, y + 1),
        (x, y + 1),
        (x + 1, y + 1),
    ]
}
