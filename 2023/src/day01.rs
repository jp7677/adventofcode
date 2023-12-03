use crate::util::*;

#[test]
fn part01() {
    let document = read_input(DAYS::Day01);

    assert_eq!(read_calibration_value(&document), 55123);
}

#[test]
fn part02() {
    let document = read_input(DAYS::Day01);

    let document = spelled_out_letters()
        .iter()
        .fold(document, |acc, (letters, digits)| {
            acc.replace(letters, digits)
        });

    assert_eq!(read_calibration_value(&document), 55260);
}

fn spelled_out_letters<'a>() -> [(&'a str, &'a str); 16] {
    [
        ("oneight", "18"),
        ("twone", "21"),
        ("treeight", "38"),
        ("fiveight", "58"),
        ("eightwo", "82"),
        ("eighthree", "83"),
        ("nineight", "98"),
        ("one", "1"),
        ("two", "2"),
        ("three", "3"),
        ("four", "4"),
        ("five", "5"),
        ("six", "6"),
        ("seven", "7"),
        ("eight", "8"),
        ("nine", "9"),
    ]
}

fn read_calibration_value(input: &str) -> u32 {
    let values = input
        .lines()
        .map(|line| {
            let c1 = line.chars().find(|c| c.is_digit(10)).unwrap();
            let c2 = line.chars().rfind(|c| c.is_digit(10)).unwrap();
            [c1, c2].iter().collect::<String>().parse::<u32>().unwrap()
        })
        .collect::<Vec<_>>();

    values.iter().sum::<u32>()
}
