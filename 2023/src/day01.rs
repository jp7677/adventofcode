#[cfg(test)]
use crate::util::*;

#[test]
fn part01() {
    let input = read_input(DAYS::Day01);

    assert_eq!(read_calibration_value(&input), 55123);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day01)
        .replace("oneight", "18")
        .replace("twone", "21")
        .replace("treeight", "38")
        .replace("fiveight", "58")
        .replace("eightwo", "82")
        .replace("eighthree", "83")
        .replace("nineight", "98")
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9");

    assert_eq!(read_calibration_value(&input), 55260);
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
