use crate::util::*;

struct Card {
    winning_numbers: Vec<u32>,
    numbers: Vec<u32>,
}

impl Card {
    fn points(&self) -> u32 {
        self.numbers.iter().fold(0, |acc, n| {
            if self.winning_numbers.contains(n) {
                if acc > 0 {
                    acc * 2
                } else {
                    1
                }
            } else {
                acc
            }
        })
    }

    fn wins(&self) -> u32 {
        self.numbers.iter().fold(0, |acc, n| {
            if self.winning_numbers.contains(n) {
                acc + 1
            } else {
                acc
            }
        })
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day04);

    let cards = parse_cards(input);
    let points = cards.iter().map(|c| c.points()).sum::<u32>();

    assert_eq!(points, 27845);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day04);

    let cards = parse_cards(input);
    let mut copies = vec![1; cards.len()];
    for (i, c) in cards.iter().enumerate() {
        let wins = c.wins();
        for _ in 0..copies[i] {
            for n in i..i + wins as usize {
                copies[n + 1] += 1;
            }
        }
    }

    assert_eq!(copies.iter().sum::<u32>(), 9496801);
}

fn parse_cards(input: String) -> Vec<Card> {
    input
        .lines()
        .map(|line| {
            let (w, n) = line.split_once(':').unwrap().1.split_once('|').unwrap();
            Card {
                winning_numbers: parse_numbers(w),
                numbers: parse_numbers(n),
            }
        })
        .collect::<Vec<Card>>()
}

fn parse_numbers(n: &str) -> Vec<u32> {
    n.split_whitespace()
        .map(|s| s.parse::<u32>().unwrap())
        .collect::<Vec<u32>>()
}
