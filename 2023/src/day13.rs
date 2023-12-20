use crate::util::*;
use std::cmp::min;
use std::collections::HashSet;

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Coord {
    x: i32,
    y: i32,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day13);

    let maps = parse_maps(&input);
    let number = maps.iter().fold(0, |acc, map| {
        let result = find_reflection(map, 0);
        acc + result.0 + 100 * result.1
    });

    assert_eq!(number, 34772);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day13);

    let maps = parse_maps(&input);
    let number = maps.iter().fold(0, |acc, map| {
        let result = find_reflection(map, 1);
        acc + result.0 + 100 * result.1
    });

    assert_eq!(number, 35554);
}

fn find_reflection(map: &HashSet<Coord>, diff: usize) -> (u32, u32) {
    let max_x = map.iter().map(|c| c.x).max().unwrap() + 1;
    for i in 1..max_x {
        let width = min(i, max_x - i);
        let a = map
            .iter()
            .filter(|c| c.x < i && c.x > i - width - 1)
            .collect::<HashSet<_>>();
        let b = map
            .iter()
            .filter(|c| c.x >= i && c.x < i + width)
            .map(|c| Coord {
                x: i - (c.x - i + 1),
                y: c.y,
            })
            .collect::<HashSet<_>>();

        if a.symmetric_difference(&b.iter().collect::<HashSet<_>>())
            .collect::<HashSet<_>>()
            .len()
            == diff
        {
            return (i as u32, 0);
        }
    }

    let max_y = map.iter().map(|c| c.y).max().unwrap() + 1;
    for i in 1..max_y {
        let height = min(i, max_y - i);
        let a = map
            .iter()
            .filter(|c| c.y < i && c.y > i - height - 1)
            .collect::<HashSet<_>>();
        let b = map
            .iter()
            .filter(|c| c.y >= i && c.y < i + height)
            .map(|c| Coord {
                x: c.x,
                y: i - (c.y - i + 1),
            })
            .into_iter()
            .collect::<HashSet<_>>();

        if a.symmetric_difference(&b.iter().collect::<HashSet<_>>())
            .collect::<HashSet<_>>()
            .len()
            == diff
        {
            return (0, i as u32);
        }
    }

    panic!("no reflection")
}

fn parse_maps(input: &String) -> Vec<HashSet<Coord>> {
    input
        .split("\n\n")
        .map(|p| {
            let mut map: HashSet<Coord> = HashSet::new();
            for (y, line) in p.lines().enumerate() {
                for (x, symbol) in line.chars().enumerate() {
                    if symbol != '.' {
                        map.insert(Coord {
                            x: x as i32,
                            y: y as i32,
                        });
                    }
                }
            }
            map
        })
        .collect()
}
