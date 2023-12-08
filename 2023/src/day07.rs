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

    pub fn cmp(&self, other: &Hand, joker: bool) -> Ordering {
        if self.cards == other.cards {
            Ordering::Equal
        } else {
            let score_self = if joker {
                self.use_joker().type_score()
            } else {
                self.type_score()
            };
            let score_other = if joker {
                other.use_joker().type_score()
            } else {
                other.type_score()
            };

            if score_self > score_other {
                Ordering::Greater
            } else if score_self < score_other {
                Ordering::Less
            } else {
                let score_self = self.cards_score(joker);
                let score_other = other.cards_score(joker);
                if score_self > score_other {
                    Ordering::Greater
                } else if score_self < score_other {
                    Ordering::Less
                } else {
                    panic!()
                }
            }
        }
    }

    pub fn use_joker(&self) -> Hand {
        if !self.cards.contains('J') {
            return Hand {
                cards: self.cards.clone(),
                card_counts: self.card_counts.clone(),
            };
        }
        if self.cards == "JJJJJ" {
            return Hand::from("AAAAA");
        }

        let card_count_excl_joker = self
            .card_counts
            .iter()
            .filter(|(k, _)| **k != 'J')
            .collect::<HashMap<&char, &u32>>();
        let max_count = card_count_excl_joker.values().max().unwrap();
        let mut highest_score_chars = card_count_excl_joker
            .iter()
            .filter(|(_, v)| *v == max_count)
            .map(|(k, _)| *k)
            .collect::<Vec<&char>>();
        highest_score_chars.sort_by(|a, b| {
            let s1 = Hand::card_score(**a, false);
            let s2 = Hand::card_score(**b, false);
            if s1 > s2 {
                Ordering::Greater
            } else if s1 < s2 {
                Ordering::Less
            } else {
                Ordering::Equal
            }
        });
        let highest_score_char = highest_score_chars.last().unwrap();

        let cards = self
            .cards
            .replace("J", highest_score_char.to_string().as_str());
        Hand::from(cards.as_str())
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

    fn cards_score(&self, joker: bool) -> u32 {
        self.cards
            .chars()
            .map(|c| Hand::card_score(c, joker))
            .fold(String::new(), |acc, c| String::from(format!("{acc}{c:02}")))
            .parse::<u32>()
            .unwrap()
    }

    fn card_score(c: char, joker: bool) -> u32 {
        match c {
            'A' => 14,
            'K' => 13,
            'Q' => 12,
            'J' => {
                if joker {
                    1
                } else {
                    11
                }
            }
            'T' => 10,
            _ => c.to_digit(10).unwrap(),
        }
    }
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day07);

    let mut games = parse_games(input);
    games.sort_by(|(a, _), (b, _)| a.cmp(b, false));
    let total_winnings = calc_winnings(&games);

    assert_eq!(total_winnings, 248105065);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day07);

    let mut games = parse_games(input);
    games.sort_by(|(a, _), (b, _)| a.cmp(b, true));
    let total_winnings = calc_winnings(&games);

    assert_eq!(total_winnings, 249515436);
}

fn parse_games(input: String) -> Vec<(Hand, u32)> {
    input
        .lines()
        .map(|line| {
            let s = line.split_once(' ').unwrap();
            (Hand::from(s.0), s.1.parse::<u32>().unwrap())
        })
        .collect::<Vec<(Hand, u32)>>()
}

fn calc_winnings(games: &Vec<(Hand, u32)>) -> u32 {
    games
        .iter()
        .enumerate()
        .fold(0, |acc, (i, (_, bid))| acc + (i as u32 + 1) * bid)
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
        Hand::from("33332").cmp(&Hand::from("2AAAA"), false),
        Ordering::Greater
    );
    assert_eq!(
        Hand::from("77888").cmp(&Hand::from("77788"), false),
        Ordering::Greater
    );
}
