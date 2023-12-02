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
        .map(|g| g.id)
        .sum::<u32>();

    assert_eq!(possible_games, 2204);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day02);

    let power_of_games = parse_games(&input)
        .iter()
        .map(|g| {
            Set {
                red: g.sets.iter().map(|s| s.red.unwrap_or_default()).max(),
                green: g.sets.iter().map(|s| s.green.unwrap_or_default()).max(),
                blue: g.sets.iter().map(|s| s.blue.unwrap_or_default()).max(),
            }
            .power()
        })
        .sum::<u32>();

    assert_eq!(power_of_games, 71036);
}

fn parse_games(input: &str) -> Vec<Game> {
    input
        .lines()
        .map(|record| {
            let parts = record.split_once(":").unwrap();

            let id = parts.0.split_once(" ").unwrap().1.parse::<u32>().unwrap();
            let sets = parts
                .1
                .split(";")
                .map(|subset| {
                    let colors = subset
                        .split(",")
                        .map(|c| {
                            let p = c.trim().split_once(" ").unwrap();
                            (p.0.trim().parse::<u32>().unwrap(), p.1)
                        })
                        .collect::<Vec<(u32, &str)>>();

                    Set {
                        red: get_color("red", &colors),
                        green: get_color("green", &colors),
                        blue: get_color("blue", &colors),
                    }
                })
                .collect::<Vec<Set>>();

            Game { id, sets }
        })
        .collect::<Vec<Game>>()
}

fn get_color(color: &str, colors: &Vec<(u32, &str)>) -> Option<u32> {
    Some(colors.iter().find(|c| c.1 == color)?.0)
}
