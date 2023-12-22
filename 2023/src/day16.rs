use crate::util::*;
use std::collections::{HashMap, HashSet};
use std::hash::Hash;

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Coord {
    x: u32,
    y: u32,
}

impl Coord {
    fn is_inside_map(&self, max_x: u32, max_y: u32) -> bool {
        self.x > 0 && self.x <= max_x && self.y > 0 && self.y <= max_y
    }
}

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
enum Dir {
    North,
    East,
    South,
    West,
}

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Pos {
    coord: Coord,
    dir: Dir,
}

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Beam {
    pos: Pos,
    finished: bool,
}

impl Beam {
    fn from(pos: Pos) -> Beam {
        Beam {
            pos,
            finished: false,
        }
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day16);

    let map = parse_map(&input);
    let max_x = map.iter().map(|(c, _)| c.x).max().unwrap();
    let max_y = map.iter().map(|(c, _)| c.y).max().unwrap();
    let initial = Pos {
        coord: Coord { x: 1, y: 1 },
        dir: Dir::South,
    };
    let energized = fire_beam(initial, &map, max_x, max_y);

    assert_eq!(energized, 8021);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day16);

    let map = parse_map(&input);
    let max_x = map.iter().map(|(c, _)| c.x).max().unwrap();
    let max_y = map.iter().map(|(c, _)| c.y).max().unwrap();

    let energized1 = (1..max_x)
        .map(|x| {
            let initial = Pos {
                coord: Coord { x, y: 1 },
                dir: Dir::South,
            };
            fire_beam(initial, &map, max_x, max_y)
        })
        .max()
        .unwrap();
    let energized2 = (1..max_y)
        .map(|y| {
            let initial = Pos {
                coord: Coord { x: 1, y },
                dir: Dir::East,
            };
            fire_beam(initial, &map, max_x, max_y)
        })
        .max()
        .unwrap();
    let energized3 = (1..max_x)
        .map(|x| {
            let initial = Pos {
                coord: Coord { x, y: max_y },
                dir: Dir::North,
            };
            fire_beam(initial, &map, max_x, max_y)
        })
        .max()
        .unwrap();
    let energized4 = (1..max_y)
        .map(|y| {
            let initial = Pos {
                coord: Coord { x: max_x, y },
                dir: Dir::West,
            };
            fire_beam(initial, &map, max_x, max_y)
        })
        .max()
        .unwrap();

    let energized = vec![energized1, energized2, energized3, energized4];
    let energized = energized.iter().max().unwrap();

    assert_eq!(*energized, 8216);
}

fn fire_beam(initial_position: Pos, map: &HashMap<Coord, char>, max_x: u32, max_y: u32) -> usize {
    let mut beam = vec![Beam::from(initial_position)];
    let mut visited = HashSet::from([beam[0].pos.clone()]);

    while beam.iter().any(|b| !b.finished) {
        let mut siblings = Vec::new();
        beam.iter_mut().filter(|b| !b.finished).for_each(|b| {
            let (first, second) = next_position(&b.pos, &map);
            if first.coord.is_inside_map(max_x, max_y) {
                if visited.contains(&first) {
                    b.finished = true;
                } else {
                    visited.insert(first.clone());
                    b.pos = first.clone();
                };

                if second != None && !visited.contains(second.as_ref().unwrap()) {
                    siblings.push(Beam::from(second.unwrap().clone()));
                }
            } else {
                b.finished = true;
            }
        });
        beam.extend(siblings.into_iter());
    }

    let visited: HashSet<&Coord> = HashSet::from_iter(visited.iter().map(|b| &b.coord));
    visited.len()
}

fn next_position(beam: &Pos, map: &HashMap<Coord, char>) -> (Pos, Option<Pos>) {
    let next = match beam.dir {
        Dir::North => Pos {
            coord: Coord {
                x: beam.coord.x,
                y: beam.coord.y - 1,
            },
            dir: beam.dir.clone(),
        },
        Dir::East => Pos {
            coord: Coord {
                x: beam.coord.x + 1,
                y: beam.coord.y,
            },
            dir: beam.dir.clone(),
        },
        Dir::South => Pos {
            coord: Coord {
                x: beam.coord.x,
                y: beam.coord.y + 1,
            },
            dir: beam.dir.clone(),
        },
        Dir::West => Pos {
            coord: Coord {
                x: beam.coord.x - 1,
                y: beam.coord.y,
            },
            dir: beam.dir.clone(),
        },
    };

    let (first, second) = match map.get(&next.coord) {
        Some(s) => match (s, &next.dir) {
            ('|', Dir::North) => (next.dir, None),
            ('|', Dir::East) => (Dir::North, Some(Dir::South)),
            ('|', Dir::South) => (next.dir, None),
            ('|', Dir::West) => (Dir::North, Some(Dir::South)),
            ('-', Dir::North) => (Dir::East, Some(Dir::West)),
            ('-', Dir::East) => (next.dir, None),
            ('-', Dir::South) => (Dir::East, Some(Dir::West)),
            ('-', Dir::West) => (next.dir, None),
            ('/', Dir::North) => (Dir::East, None),
            ('/', Dir::East) => (Dir::North, None),
            ('/', Dir::South) => (Dir::West, None),
            ('/', Dir::West) => (Dir::South, None),
            ('\\', Dir::North) => (Dir::West, None),
            ('\\', Dir::East) => (Dir::South, None),
            ('\\', Dir::South) => (Dir::East, None),
            ('\\', Dir::West) => (Dir::North, None),
            _ => panic!("unknown move"),
        },
        None => (next.dir, None),
    };

    (
        Pos {
            coord: next.coord.clone(),
            dir: first.clone(),
        },
        match second {
            Some(v) => Some(Pos {
                coord: next.coord.clone(),
                dir: v,
            }),
            None => None,
        },
    )
}

fn parse_map(input: &String) -> HashMap<Coord, char> {
    let mut map = HashMap::new();
    for (y, line) in input.lines().enumerate() {
        for (x, symbol) in line.chars().enumerate() {
            if symbol != '.' {
                map.insert(
                    Coord {
                        x: x as u32 + 1,
                        y: y as u32 + 1,
                    },
                    symbol,
                );
            }
        }
    }
    map
}
