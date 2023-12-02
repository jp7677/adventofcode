#[cfg(test)]
use crate::util::*;

struct Set {
    red: Option<u32>,
    green: Option<u32>,
    blue: Option<u32>,
}

impl Set {
    fn power(&self) -> u32 {
        self.red.unwrap_or(1) * self.green.unwrap_or(1) * self.blue.unwrap_or(1)
    }
}

struct Game {
    id: u32,
    sets: Vec<Set>,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day02);

    let possible_games = parse_games(&input)
        .iter()
        .filter(|g| {
            g.sets.iter().all(|s| {
                s.red.unwrap_or_default() <= 12
                    && s.green.unwrap_or_default() <= 13
                    && s.blue.unwrap_or_default() <= 14
            })
        })
        .fold(0, |acc, g| acc + g.id);

    assert_eq!(possible_games, 2204);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day02);

    let power_of_games = parse_games(&input).iter().fold(0, |acc, g| {
        acc + Set {
            red: g.sets.iter().map(|s| s.red.unwrap_or_default()).max(),
            green: g.sets.iter().map(|s| s.green.unwrap_or_default()).max(),
            blue: g.sets.iter().map(|s| s.blue.unwrap_or_default()).max(),
        }
        .power()
    });

    assert_eq!(power_of_games, 71036);
}

fn parse_games(input: &str) -> Vec<Game> {
    input.lines().map(|it| create_game(it).unwrap()).collect()
}

fn create_game(record: &str) -> Option<Game> {
    let (id, sets) = record.split_once(":")?;
    Some(Game {
        id: id.split_once(" ")?.1.parse::<u32>().ok()?,
        sets: sets.split(";").map(|it| create_set(it)).collect(),
    })
}

fn create_set(s: &str) -> Set {
    let colors = s
        .split(",")
        .map(|it| create_color(it).unwrap())
        .collect::<Vec<(u32, &str)>>();

    Set {
        red: get_cubes("red", &colors),
        green: get_cubes("green", &colors),
        blue: get_cubes("blue", &colors),
    }
}

fn create_color(s: &str) -> Option<(u32, &str)> {
    let (num, color) = s.trim().split_once(" ")?;
    Some((num.trim().parse::<u32>().ok()?, color))
}

fn get_cubes(color: &str, colors: &Vec<(u32, &str)>) -> Option<u32> {
    Some(colors.iter().find(|c| c.1 == color)?.0)
}
