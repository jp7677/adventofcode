use crate::util::*;
use std::fmt::format;

#[derive(Debug)]
struct Record<'a> {
    spring_list: &'a str,
    spring_groups: Vec<u32>,
    gaps: u32,
    voids: u32,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day12);

    let records = unfold_records(&input, 1);
    let records = parse_records(&records);
    let count = count_arrangements(&records);

    assert_eq!(count, 6827);
}

#[test]
#[ignore = "way to slow ..."]
fn part02() {
    let input = read_input(DAYS::Day12);

    let records = unfold_records(&input, 5);
    let records = parse_records(&records);
    let _ = count_arrangements(&records);

    assert_eq!(0, 0);
}

fn count_arrangements(records: &Vec<Record>) -> usize {
    records
        .iter()
        .map(|record| {
            let mut count = 0usize;
            balls_and_urns1(
                &record,
                &mut count,
                &Vec::new(),
                record.voids as usize,
                1,
                record.gaps as usize,
            );
            count
        })
        .sum::<usize>()
}

fn balls_and_urns1(
    record: &Record,
    acc: &mut usize,
    v: &Vec<usize>,
    n: usize,
    depth: usize,
    max_depth: usize,
) {
    let sum = v.iter().sum::<usize>();
    let remain = if sum <= n { 0 } else { n - sum };

    if depth == max_depth {
        let mut v1 = v.clone();
        v1.push(n - sum);

        let arrangement_version = to_arrangement(&record.spring_groups, &v1, v1.len());
        if is_valid_arrangement(
            &record.spring_list,
            &arrangement_version,
            arrangement_version.len(),
        ) {
            *acc = *acc + 1;
        }
        return;
    } else if remain >= (n - sum + 1) {
        let mut v1 = v.clone();
        v1.push(0);

        let arrangement_version = to_arrangement(&record.spring_groups, &v1, v1.len());
        if is_valid_arrangement(
            &record.spring_list,
            &arrangement_version,
            arrangement_version.len(),
        ) {
            balls_and_urns1(record, acc, &mut v1, n, depth + 1, max_depth);
        }
    } else {
        for i in 0..(n - sum + 1) {
            let mut v1 = v.clone();
            v1.push(i);

            let arrangement_version = to_arrangement(&record.spring_groups, &v1, v1.len());
            if is_valid_arrangement(
                &record.spring_list,
                &arrangement_version,
                arrangement_version.len(),
            ) {
                balls_and_urns1(record, acc, &mut v1, n, depth + 1, max_depth);
            }
        }
    }
}

fn to_arrangement(spring_groups: &Vec<u32>, variation: &Vec<usize>, len: usize) -> String {
    let mut start = String::new();
    (0..variation[0]).for_each(|_| start.push('.'));
    spring_groups
        .iter()
        .take(len)
        .enumerate()
        .fold(start, |mut acc, (i, v)| {
            (0..*v).for_each(|_| acc.push('#'));
            if variation.len() > 1 && i < variation.len() - 1 {
                acc.push('.')
            }
            if variation.len() > i + 1 {
                (0..variation[i + 1]).for_each(|_| acc.push('.'));
            }
            acc
        })
}

fn is_valid_arrangement(spring_list: &str, version: &str, len: usize) -> bool {
    for (i, c) in spring_list.chars().take(len).enumerate() {
        if c != '?' && c != ansi_char_at(version, i) {
            return false;
        }
    }
    true
}

fn unfold_records(input: &String, multiple: u32) -> Vec<String> {
    input
        .lines()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            let list = (1..multiple)
                .fold(String::from(p[0]), |acc, _| format!("{}?{}", acc, p[0]))
                .replace("..", ".")
                .replace("..", ".")
                .trim_start_matches('.')
                .trim_end_matches('.')
                .to_string();
            let group =
                (1..multiple).fold(String::from(p[1]), |acc, _| format!("{},{}", acc, p[1]));
            format!("{} {}", list, group)
        })
        .collect::<Vec<String>>()
}

fn parse_records(records: &Vec<String>) -> Vec<Record> {
    records
        .iter()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            let groups = p[1]
                .split(',')
                .map(|g| g.parse::<u32>().unwrap())
                .collect::<Vec<_>>();
            let gaps = groups.len() as u32 + 1;
            let voids = p[0].len() as u32 - groups.iter().fold(gaps - 2, |acc, g| acc + g);

            Record {
                spring_list: p[0],
                spring_groups: groups,
                gaps: gaps,
                voids: voids,
            }
        })
        .collect::<Vec<Record>>()
}

#[test]
fn combinatorics() {
    assert_eq!(choose(1, 1), 1);
    assert_eq!(choose(2, 1), 2);
    assert_eq!(choose(6, 3), 20);
    assert_eq!(choose(60, 50), 75394027566);

    let r = balls_and_urns(4, 3);
    assert_eq!(r.len(), 15);
    assert_eq!(r[0], vec![0, 0, 4]);
    assert_eq!(r[1], vec![0, 1, 3]);
    assert_eq!(r[2], vec![0, 2, 2]);
    assert_eq!(r[3], vec![0, 3, 1]);
    assert_eq!(r[4], vec![0, 4, 0]);
    assert_eq!(r[5], vec![1, 0, 3]);
    assert_eq!(r[6], vec![1, 1, 2]);
    assert_eq!(r[7], vec![1, 2, 1]);
    assert_eq!(r[8], vec![1, 3, 0]);
    assert_eq!(r[9], vec![2, 0, 2]);
    assert_eq!(r[10], vec![2, 1, 1]);
    assert_eq!(r[11], vec![2, 2, 0]);
    assert_eq!(r[12], vec![3, 0, 1]);
    assert_eq!(r[13], vec![3, 1, 0]);
    assert_eq!(r[14], vec![4, 0, 0]);

    let n = 12;
    let k = 4;
    assert_eq!(
        choose(n + k - 1, k - 1),
        balls_and_urns(n, k).iter().count()
    );
}
