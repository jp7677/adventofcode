use crate::util::*;
use std::collections::{HashMap, HashSet};
use std::ops::Deref;

#[derive(Debug, Eq, Hash, PartialEq, PartialOrd, Clone)]
struct Coord {
    x: i32,
    y: i32,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day17);

    let map = parse_map(&input);
    let heat_loss = find_lowest_heat_path(&map);

    assert_eq!(heat_loss, 102);
}

fn find_lowest_heat_path(map: &HashMap<Coord, u32>) -> u32 {
    let direction = vec![
        Coord { x: 1, y: 0 },
        Coord { x: -1, y: 0 },
        Coord { x: 0, y: 1 },
        Coord { x: 0, y: -1 },
    ];
    let max_x = map.iter().map(|(c, _)| c.x).max().unwrap();
    let max_y = map.iter().map(|(c, _)| c.y).max().unwrap();
    let start = Coord { x: 0, y: 0 };
    let end = Coord { x: max_x, y: max_y };

    let mut queue = HashMap::from([(start.clone(), 0)]);
    let mut totals = HashMap::from([(start.clone(), (vec![start.clone()], 0))]);
    let mut visited = HashSet::new();

    while queue.len() > 0 {
        let current = queue.values().min().unwrap();
        let current = queue.iter().find(|(_, v)| *v == current).unwrap();
        let current_coord = current.0.clone();
        let current_heat = *current.1;
        let (current_path, _) = totals.get(&current_coord).unwrap();
        let current_path = current_path.to_vec();

        direction
            .iter()
            .map(|direction| Coord {
                x: current_coord.x + direction.x,
                y: current_coord.y + direction.y,
            })
            .filter(|coord| !visited.contains(coord))
            .map(|coord| (coord.clone(), map.get(&coord)))
            .filter(|(_, heat)| *heat != None)
            .for_each(|(coord, heat)| {
                let total = current_heat + heat.unwrap();
                let op = (Vec::new(), u32::MAX);
                let (_, known_total) = totals.get(&coord).or(Some(&op)).unwrap();
                if total < *known_total {
                    let mut path = current_path.to_vec();
                    path.push(coord.clone());
                    totals.insert(coord.clone(), (path, total));
                    queue.insert(coord.clone(), total);
                } else {
                    queue.insert(coord.clone(), *known_total);
                }
            });

        if current_coord == end {
            break;
        }
        visited.insert(current_coord.clone());
        queue.remove(&current_coord);
    }

    let (_, lowest_heat) = &totals[&end];
    *lowest_heat
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
}

fn parse_map(input: &String) -> HashMap<Coord, u32> {
    let mut map = HashMap::new();
    for (y, line) in input.lines().enumerate() {
        for (x, heat_loss) in line.chars().enumerate() {
            map.insert(
                Coord {
                    x: x as i32,
                    y: y as i32,
                },
                heat_loss.to_digit(10).unwrap(),
            );
        }
    }
    map
}
