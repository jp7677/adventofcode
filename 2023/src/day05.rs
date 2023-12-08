use crate::util::*;
use std::ops::Deref;

struct Converter {
    dest_start: u64,
    src_start: u64,
    length: u64,
}

struct Map {
    from: String,
    to: String,
    converters: Vec<Converter>,
}

impl Map {
    fn map_number(&self, number: u64) -> u64 {
        for c in &self.converters {
            if number >= c.src_start && number < c.src_start + c.length {
                return c.dest_start + (number - c.src_start);
            }
        }
        return number;
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day05);

    let (seeds, maps) = parse_seeds_and_maps(&input);
    let mut stage = "seed";
    let mut numbers = seeds.to_vec();
    while stage != "location" {
        let map = maps.iter().find(|m| m.from == stage).unwrap();
        numbers = numbers
            .iter()
            .map(|s| map.map_number(*s))
            .collect::<Vec<u64>>();
        stage = map.to.as_str();
    }
    let lowest_location = numbers.iter().min().unwrap();

    assert_eq!(*lowest_location, 111627841);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day05);

    assert_eq!(input.lines().count(), 1);
}

fn parse_seeds_and_maps(input: &String) -> (Vec<u64>, Vec<Map>) {
    let seeds = input
        .lines()
        .next()
        .unwrap()
        .split_ascii_whitespace()
        .skip(1);
    let seeds = seeds
        .map(|c| c.parse::<u64>().unwrap())
        .collect::<Vec<u64>>();

    let maps = input
        .split("\n\n")
        .skip(1)
        .map(|m| {
            let desc = m
                .lines()
                .next()
                .unwrap()
                .split_once(' ')
                .unwrap()
                .0
                .split('-')
                .collect::<Vec<&str>>();

            let converters = m.lines().skip(1).map(|c| {
                let p = c
                    .split(' ')
                    .map(|n| n.parse::<u64>().unwrap())
                    .collect::<Vec<u64>>();
                Converter {
                    dest_start: p[0],
                    src_start: p[1],
                    length: p[2],
                }
            });

            Map {
                from: String::from(*desc.first().unwrap()),
                to: String::from(*desc.last().unwrap()),
                converters: converters.collect(),
            }
        })
        .collect::<Vec<Map>>();

    (seeds, maps)
}
