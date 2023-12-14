use crate::util::*;
use std::fmt::format;

struct Record<'a> {
    spring_list: &'a str,
    spring_groups: &'a str,
}

#[test]
#[ignore = "to slow ..."]
fn part01() {
    let input = read_input(DAYS::Day12);

    let records = input
        .lines()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            Record {
                spring_list: p[0],
                spring_groups: p[1],
            }
        })
        .collect::<Vec<Record>>();
    let arrangement_count = count_arrangements(records);

    assert_eq!(arrangement_count, 6827);
}

#[test]
#[ignore = "way to slow ..."]
fn part02() {
    let input = read_input(DAYS::Day12);

    let records = input
        .lines()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            let list = (1..5).fold(String::from(p[0]), |acc, _| format!("{}?{}", acc, p[0]));
            let group = (1..5).fold(String::from(p[1]), |acc, _| format!("{},{}", acc, p[1]));
            format!("{} {}", list, group)
        })
        .collect::<Vec<String>>();

    let records = records
        .iter()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            Record {
                spring_list: p[0],
                spring_groups: p[1],
            }
        })
        .collect::<Vec<Record>>();
    let arrangement_count = count_arrangements(records);

    assert_eq!(0, 0);
}

fn count_arrangements(records: Vec<Record>) -> usize {
    let arrangement_count = records
        .iter()
        .map(|r| {
            let damages = r.spring_list.chars().filter(|c| *c == '?').count();
            let bits = (0..damages).fold(0, |acc, _| (acc << 1) + 1);
            let combinations = (0..bits + 1)
                .map(|b| String::from(format!("{:0>num$}", format!("{:b}", b), num = damages)))
                .map(|b| b.replace("0", ".").replace("1", "#"))
                .collect::<Vec<String>>();

            let combinations = combinations
                .iter()
                .map(|combination| {
                    let mut i = 0;
                    r.spring_list.chars().fold(String::new(), |acc, c| {
                        let c = if c == '?' {
                            i = i + 1;
                            ansi_char_at(combination, i - 1)
                        } else {
                            c
                        };
                        format!("{}{}", acc, c)
                    })
                })
                .collect::<Vec<String>>();

            combinations
                .iter()
                .filter(|combination| combination.contains('#'))
                .map(|combination| {
                    let arrangement = combination
                        .split(".")
                        .filter(|v| v.len() > 0)
                        .fold(String::new(), |acc, v| format!("{},{}", acc, v.len()));
                    let arrangement = String::from(&arrangement[1..]);
                    arrangement == r.spring_groups
                })
                .filter(|c| *c == true)
                .count()
        })
        .sum::<usize>();
    arrangement_count
}
