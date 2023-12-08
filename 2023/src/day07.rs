use crate::util::*;
use std::cmp::Ordering;
use std::collections::HashMap;

struct Hand {
    cards: String,
    card_counts: HashMap<char, u32>,
}

impl Hand {
    pub fn from(cards: &str) -> Hand {
        Hand {
            cards: String::from(cards),
            card_counts: char_count(cards),
        }
    }

    pub fn cmp(&self, other: &Hand) -> Ordering {
        if self.cards == other.cards {
            Ordering::Equal
        } else {
            let s1 = self.type_score();
            let s2 = other.type_score();
            if s1 > s2 {
                Ordering::Greater
            } else if s1 < s2 {
                Ordering::Less
            } else {
                let s1 = self.cards_score();
                let s2 = other.cards_score();
                if s1 > s2 {
                    Ordering::Greater
                } else if s1 < s2 {
                    Ordering::Less
                } else {
                    panic!()
                }
            }
        }
    }

    fn type_score(&self) -> u16 {
        if self.is_five_of_a_kind() {
            7
        } else if self.is_four_of_a_kind() {
            6
        } else if self.is_full_house() {
            5
        } else if self.is_three_of_a_kind() {
            4
        } else if self.is_two_pairs() {
            3
        } else if self.is_one_pairs() {
            2
        } else if self.is_high_card() {
            1
        } else {
            0
        }
    }

    fn is_five_of_a_kind(&self) -> bool {
        self.card_counts.len() == 1
    }

    fn is_four_of_a_kind(&self) -> bool {
        self.card_counts.values().any(|v| *v == 4)
    }

    fn is_full_house(&self) -> bool {
        self.card_counts.values().any(|v| *v == 3) && self.card_counts.values().any(|v| *v == 2)
    }

    fn is_three_of_a_kind(&self) -> bool {
        self.card_counts.values().filter(|v| **v == 3).count() == 1
    }

    fn is_two_pairs(&self) -> bool {
        self.card_counts.values().filter(|v| **v == 2).count() == 2
    }

    fn is_one_pairs(&self) -> bool {
        self.card_counts.values().filter(|v| **v == 2).count() == 1
    }

    fn is_high_card(&self) -> bool {
        self.card_counts.values().all(|v| *v == 1)
    }

    fn cards_score(&self) -> u32 {
        self.cards
            .chars()
            .map(|c| Hand::card_score(c))
            .fold(String::new(), |acc, c| String::from(format!("{acc}{c:02}")))
            .parse::<u32>()
            .unwrap()
    }

    fn card_score(c: char) -> u32 {
        match c {
            'A' => 14,
            'K' => 13,
            'Q' => 12,
            'J' => 11,
            'T' => 10,
            _ => c.to_digit(10).unwrap(),
        }
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day07);

    let mut games = input
        .lines()
        .map(|line| {
            let s = line.split_once(' ').unwrap();
            (Hand::from(s.0), s.1.parse::<u32>().unwrap())
        })
        .collect::<Vec<(Hand, u32)>>();

    games.sort_by(|(a, _), (b, _)| a.cmp(b));

    let total_winnings = games
        .iter()
        .enumerate()
        .fold(0, |acc, (i, (_, bid))| acc + (i as u32 + 1) * bid);

    assert_eq!(total_winnings, 248105065);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
}

#[test]
fn five_of_a_kind() {
    assert_eq!(Hand::from("AAAAA").is_five_of_a_kind(), true);
}

#[test]
fn four_of_a_kind() {
    assert_eq!(Hand::from("AA8AA").is_four_of_a_kind(), true);
}

#[test]
fn full_house() {
    assert_eq!(Hand::from("23332").is_full_house(), true);
}

#[test]
fn three_of_a_kind() {
    assert_eq!(Hand::from("TTT98").is_three_of_a_kind(), true);
}

#[test]
fn two_pair() {
    assert_eq!(Hand::from("23432").is_two_pairs(), true);
}

#[test]
fn one_pair() {
    assert_eq!(Hand::from("A23A4").is_one_pairs(), true);
}

#[test]
fn high_card() {
    assert_eq!(Hand::from("23456").is_high_card(), true);
}

#[test]
fn same_type() {
    assert_eq!(
        Hand::from("33332").cmp(&Hand::from("2AAAA")),
        Ordering::Greater
    );
    assert_eq!(
        Hand::from("77888").cmp(&Hand::from("77788")),
        Ordering::Greater
    );
}
