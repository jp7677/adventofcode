use crate::util::*;
#[cfg(test)]
use std::rc::Rc;

struct Coord {
    x: i32,
    y: i32,
    symbol: char,
}

struct Part {
    number: String,
    adjacents: Vec<Rc<Coord>>,
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
        .filter(|n| n.adjacents.len() > 0)
        .fold(0, |acc, p| acc + p.number());

    assert_eq!(sum_of_parts, 528799);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day03);
    let parts = parse_map(&input);

    let mut gears = parts
        .iter()
        .filter_map(|n| n.adjacents.iter().find(|a| a.symbol == '*'))
        .map(|c| (c.x, c.y))
        .collect::<Vec<(i32, i32)>>();

    gears.sort();
    gears.dedup();

    let sum_of_gear_parts = gears.iter().fold(0, |acc, g| {
        let gear_parts = parts
            .iter()
            .filter(|p| p.adjacents.iter().any(|a| a.x == g.0 && a.y == g.1))
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
    let mut map: Vec<Rc<Coord>> = vec![];
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol != '.' {
                let coord = Rc::new(Coord {
                    x: x as i32,
                    y: y as i32,
                    symbol,
                });
                map.push(coord);
            }
        }
    }

    let map_size = input.lines().count() as i32;
    let mut parts: Vec<Part> = vec![];
    for (i, c) in input.chars().enumerate() {
        if c.is_digit(10) {
            let x = i as i32 % (map_size + 1);
            let y = i as i32 / (map_size + 1);

            let adjacents = adjacent(x, y)
                .iter()
                .filter_map(|(a_x, a_y)| {
                    let adjacent_coord = map.iter().find(|c| c.x == *a_x && c.y == *a_y);
                    match adjacent_coord {
                        None => None,
                        Some(v) => {
                            if !v.symbol.is_digit(10) {
                                Some(Rc::clone(v))
                            } else {
                                None
                            }
                        }
                    }
                })
                .collect::<Vec<Rc<Coord>>>();

            if i >= 1 && input.chars().nth(i - 1).unwrap_or('_').is_digit(10) {
                parts.last_mut().unwrap().number.push(c);
                parts.last_mut().unwrap().adjacents.extend(adjacents);
            } else {
                parts.push(Part {
                    number: c.to_string(),
                    adjacents,
                });
            }
        }
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
