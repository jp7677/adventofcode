use crate::util::*;
use std::str::Lines;

struct Race {
    time: u64,
    distance: u64,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day06);

    let mut it = input.lines();
    let times = parse_next_numbers(&mut it);
    let distances = parse_next_numbers(&mut it);

    let races = times
        .iter()
        .enumerate()
        .map(|(i, t)| Race {
            time: *t,
            distance: *distances.iter().nth(i).unwrap(),
        })
        .collect::<Vec<Race>>();

    let wins = races
        .iter()
        .map(|r| {
            (1..r.time)
                .map(|button_time| (r.time - button_time) * button_time)
                .filter(|result| result > &r.distance)
                .count()
        })
        .collect::<Vec<usize>>();

    let ways_to_win = wins.iter().fold(1, |acc, w| acc * w);

    assert_eq!(ways_to_win, 1083852);
}

fn parse_next_numbers(it: &mut Lines) -> Vec<u64> {
    it.next()
        .unwrap()
        .split_ascii_whitespace()
        .skip(1)
        .map(|s| s.parse::<u64>().unwrap())
        .collect::<Vec<u64>>()
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day06);

    let mut it = input.lines();
    let race = Race {
        time: parse_next_number(&mut it),
        distance: parse_next_number(&mut it),
    };

    let wins = (1..race.time)
        .map(|button_time| (race.time - button_time) * button_time)
        .filter(|result| result > &race.distance)
        .count();

    assert_eq!(wins, 23501589);
}

fn parse_next_number(it: &mut Lines) -> u64 {
    it.next()
        .unwrap()
        .split_ascii_whitespace()
        .skip(1)
        .fold(String::new(), |acc, s| format!("{acc}{s}"))
        .parse::<u64>()
        .unwrap()
}
