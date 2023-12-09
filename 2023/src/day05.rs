use crate::util::*;
use std::collections::HashMap;
use std::ops::Range;
use std::thread;
use std::thread::JoinHandle;

#[derive(Clone)]
struct Converter {
    dest_start: u64,
    src_start: u64,
    length: u64,
}

#[derive(Clone)]
struct Map {
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
    let lowest_location = seeds.iter().map(|s| plant(&maps, &[*s])).min().unwrap();

    assert_eq!(lowest_location, 111627841);
}

#[test]
#[ignore = "brute-force is not the correct way..."]
fn part02() {
    let input = read_input(DAYS::Day05);

    let (seeds, maps) = parse_seeds_and_maps(&input);

    let seeds = seeds.chunks(2).collect::<Vec<_>>();
    let handles = seeds
        .iter()
        .map(|seeds| {
            let maps_copy = maps.clone();
            let seeds = (seeds[0]..seeds[0] + seeds[1]).collect::<Vec<_>>();
            thread::spawn(move || plant(&maps_copy, &seeds))
        })
        .collect::<Vec<_>>();

    let lowest_location = handles
        .into_iter()
        .map(|h| h.join().unwrap())
        .min()
        .unwrap();

    assert_eq!(lowest_location, 69323688);
}

fn plant(maps: &HashMap<String, Map>, seeds: &[u64]) -> u64 {
    let mut stage = "seed";
    let mut numbers = Vec::from(seeds);
    while stage != "location" {
        let map = maps.get(stage).unwrap();
        numbers = numbers.iter().map(|s| map.map_number(*s)).collect();
        stage = map.to.as_str();
    }
    *numbers.iter().min().unwrap()
}

fn parse_seeds_and_maps(input: &String) -> (Vec<u64>, HashMap<String, Map>) {
    let seeds = input
        .lines()
        .next()
        .unwrap()
        .split_ascii_whitespace()
        .skip(1);
    let seeds = seeds.map(|c| c.parse::<u64>().unwrap()).collect();

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
                .collect::<Vec<_>>();

            let converters = m.lines().skip(1).map(|c| {
                let p = c
                    .split(' ')
                    .map(|n| n.parse::<u64>().unwrap())
                    .collect::<Vec<_>>();
                Converter {
                    dest_start: p[0],
                    src_start: p[1],
                    length: p[2],
                }
            });

            (
                String::from(*desc.first().unwrap()),
                Map {
                    to: String::from(*desc.last().unwrap()),
                    converters: converters.collect(),
                },
            )
        })
        .collect::<HashMap<String, Map>>();

    (seeds, maps)
}
