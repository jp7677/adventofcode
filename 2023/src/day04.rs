use crate::util::*;

struct Card {
    winning_numbers: Vec<u32>,
    numbers: Vec<u32>,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day04);

    let cards = parse_cards(input);
    let points = cards
        .iter()
        .map(|c| {
            c.numbers.iter().fold(0, |acc, n| {
                if c.winning_numbers.contains(n) {
                    if acc > 0 {
                        acc * 2
                    } else {
                        1
                    }
                } else {
                    acc
                }
            })
        })
        .sum::<u32>();

    assert_eq!(points, 27845);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day04);

    let cards = parse_cards(input);
    let mut copies = vec![1; cards.len()];
    for (i, c) in cards.iter().enumerate() {
        let wins = c.numbers.iter().fold(0, |acc, n| {
            if c.winning_numbers.contains(n) {
                acc + 1
            } else {
                acc
            }
        });

        for _ in 0..copies[i] {
            for n in i..i + wins {
                copies[n + 1] = copies[n + 1] + 1;
            }
        }
    }

    assert_eq!(copies.iter().sum::<u32>(), 9496801);
}

fn parse_cards(input: String) -> Vec<Card> {
    let cards = input
        .lines()
        .map(|line| {
            let (w, h) = line.split_once(':').unwrap().1.split_once('|').unwrap();
            Card {
                winning_numbers: w
                    .split_whitespace()
                    .map(|s| s.parse::<u32>().unwrap())
                    .collect::<Vec<u32>>(),
                numbers: h
                    .split_whitespace()
                    .map(|s| s.parse::<u32>().unwrap())
                    .collect::<Vec<u32>>(),
            }
        })
        .collect::<Vec<Card>>();
    cards
}
