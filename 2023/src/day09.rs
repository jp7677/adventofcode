use crate::util::*;

#[test]
fn part01() {
    let input = read_input(DAYS::Day09);

    let sum_of_values = parse_histories(&input).fold(0, |acc, history| acc + next_value(&history));

    assert_eq!(sum_of_values, 1853145119);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day09);

    let sum_of_values = parse_histories(&input)
        .map(|h| h.iter().rev().map(|v| *v).collect::<Vec<i32>>())
        .fold(0, |acc, history| acc + next_value(&history));

    assert_eq!(sum_of_values, 923);
}

fn next_value(history: &[i32]) -> i32 {
    let mut next_line = history.to_vec();
    let mut values = Vec::new();
    while !next_line.iter().all(|v| *v == 0) {
        next_line = next_line
            .windows(2)
            .map(|w| w[1] - w[0])
            .collect::<Vec<i32>>();

        values.push(*next_line.last().unwrap());
    }

    return values.iter().rev().fold(0, |acc, it| acc + it) + history.last().unwrap();
}

fn parse_histories<'a>(input: &'a str) -> impl Iterator<Item = Vec<i32>> + 'a {
    input.lines().map(|line| {
        line.split_whitespace()
            .map(|p| p.parse::<i32>().unwrap())
            .collect::<Vec<i32>>()
    })
}
