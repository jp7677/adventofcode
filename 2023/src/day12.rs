use crate::util::*;
use std::collections::HashMap;
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
    let records = parse_records(&records, 1);
    let arrangement_count = records
        .iter()
        .flat_map(|record| count_arrangements(record, 0, 1, true))
        .map(|(_, v)| v)
        .sum::<u64>();

    assert_eq!(arrangement_count, 6827);
}

#[test]
#[ignore = "should be faster ..."]
fn part02() {
    let input = read_input(DAYS::Day12);

    let records = unfold_records(&input, 5);
    let records = parse_records(&records, 5);
    let arrangement_count = records
        .iter()
        .flat_map(|record| {
            (0..5).fold(vec![HashMap::from([(0, 1u64)])], |acc, r| {
                let intermediate = acc
                    .iter()
                    .flat_map(|m| {
                        m.iter()
                            .map(|(k, v)| count_arrangements(record, *k, *v, r == 4))
                            .collect::<Vec<HashMap<u32, u64>>>()
                    })
                    .collect::<Vec<HashMap<u32, u64>>>();
                consolidate(&intermediate)
            })
        })
        .map(|m| m.values().sum::<u64>())
        .sum::<u64>();

    assert_eq!(arrangement_count, 1537505634471);
}

fn consolidate(intermediate: &Vec<HashMap<u32, u64>>) -> Vec<HashMap<u32, u64>> {
    let mut acc = HashMap::new();
    intermediate.iter().for_each(|i| {
        i.iter().for_each(|(k, v)| {
            acc.insert(*k, acc.get(k).or(Some(&0)).unwrap() + v);
        });
    });
    vec![acc]
}

fn count_arrangements(record: &Record, start: u32, multiple: u64, last: bool) -> HashMap<u32, u64> {
    let mut count = HashMap::new();
    balls_and_urns1(
        &record,
        &mut count,
        &Vec::new(),
        record.voids,
        1,
        record.gaps,
        start,
        multiple,
        last,
    );
    count
}

fn balls_and_urns1(
    record: &Record,
    acc: &mut HashMap<u32, u64>,
    v: &Vec<u32>,
    n: u32,
    depth: u32,
    max_depth: u32,
    start: u32,
    multiple: u64,
    last: bool,
) {
    let sum = v.iter().sum::<u32>();
    let remain = if sum <= n { 0 } else { n - sum };

    if depth == max_depth {
        let mut v1 = v.clone();
        v1.push(n - sum);

        let mut version = to_arrangement(&record.spring_groups, &v1);
        version = version.trim_end_matches('.').to_string();
        version.push('.');
        if is_valid_arrangement(&record.spring_list, start as usize, last, &version) {
            acc.insert(
                start + version.len() as u32,
                acc.get(&(start + version.len() as u32))
                    .or(Some(&0))
                    .unwrap()
                    + multiple,
            );
        }
        return;
    } else if remain >= (n - sum + 1) {
        let mut v1 = v.clone();
        v1.push(0);

        let version = to_arrangement(&record.spring_groups, &v1);
        if is_valid_arrangement(&record.spring_list, start as usize, false, &version) {
            balls_and_urns1(
                record,
                acc,
                &mut v1,
                n,
                depth + 1,
                max_depth,
                start,
                multiple,
                last,
            );
        }
    } else {
        for i in 0..(n - sum + 1) {
            let mut v1 = v.clone();
            v1.push(i);

            let version = to_arrangement(&record.spring_groups, &v1);
            if is_valid_arrangement(&record.spring_list, start as usize, false, &version) {
                balls_and_urns1(
                    record,
                    acc,
                    &mut v1,
                    n,
                    depth + 1,
                    max_depth,
                    start,
                    multiple,
                    last,
                );
            }
        }
    }
}

fn to_arrangement(spring_groups: &Vec<u32>, variation: &Vec<u32>) -> String {
    let mut start = String::new();
    (0..variation[0]).for_each(|_| start.push('.'));
    spring_groups
        .iter()
        .take(variation.len())
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

fn is_valid_arrangement(spring_list: &str, start: usize, last: bool, version: &str) -> bool {
    if start > spring_list.len() {
        return false;
    }

    for (i, c) in spring_list
        .chars()
        .skip(start)
        .take(version.len())
        .enumerate()
    {
        if c != '?' && c != ansi_char_at(version, i) {
            return false;
        }
    }

    if version.len() > spring_list.len() - start
        && version[spring_list.len() - start..].contains("#")
    {
        return false;
    }

    if last
        && spring_list.len() > (version.len() + start)
        && spring_list[version.len() + start..].contains("#")
    {
        return false;
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
            let group = p[1];
            format!("{} {}", list, group)
        })
        .collect::<Vec<String>>()
}

fn parse_records(records: &Vec<String>, multiple: u32) -> Vec<Record> {
    records
        .iter()
        .map(|line| {
            let p = line.split_whitespace().collect::<Vec<_>>();
            let groups = p[1]
                .split(',')
                .map(|g| g.parse::<u32>().unwrap())
                .collect::<Vec<_>>();
            let gaps = groups.len() as u32 + 1;
            let voids = p[0].len() as u32
                - (groups.iter().fold(gaps - 2, |acc, g| acc + g) * multiple)
                - (multiple - 1);

            Record {
                spring_list: p[0],
                spring_groups: groups,
                gaps,
                voids,
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
