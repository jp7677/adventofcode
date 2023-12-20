use crate::util::*;

#[test]
fn part01() {
    let sum = read_input(DAYS::Day15)
        .trim()
        .split(",")
        .fold(0, |acc, s| acc + hash_algorithm(s));

    assert_eq!(sum, 513643);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
}

#[test]
fn test_hash() {
    let hash = hash_algorithm(&"HASH");

    assert_eq!(hash, 52);
}

fn hash_algorithm(input: &str) -> u32 {
    input.chars().fold(0, |acc, c| hash_char(acc, c))
}

fn hash_char(current: u32, c: char) -> u32 {
    ((current + c as u32) * 17) % 256
}
