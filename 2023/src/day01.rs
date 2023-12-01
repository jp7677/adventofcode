#[cfg(test)]
use crate::util::*;

#[test]
fn part01() {
    let input = read_input(DAYS::Day01);

    assert_eq!(read_calibration_value(input), 55123);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day01);

    let input = input.replace("oneight", "18");
    let input = input.replace("twone", "21");
    let input = input.replace("treeight", "38");
    let input = input.replace("fiveight", "58");
    let input = input.replace("eightwo", "82");
    let input = input.replace("eighthree", "83");
    let input = input.replace("nineight", "98");
    let input = input.replace("one", "1");
    let input = input.replace("two", "2");
    let input = input.replace("three", "3");
    let input = input.replace("four", "4");
    let input = input.replace("five", "5");
    let input = input.replace("six", "6");
    let input = input.replace("seven", "7");
    let input = input.replace("eight", "8");
    let input = input.replace("nine", "9");

    assert_eq!(read_calibration_value(input), 55260);
}

fn read_calibration_value(input: String) -> u32 {
    let values = input
        .lines()
        .map(|line| {
            let c1 = line.chars().find(|c| c.is_digit(10)).unwrap();
            let c2 = line.chars().rfind(|c| c.is_digit(10)).unwrap();
            let s = [c1, c2].iter().collect::<String>();
            s.parse::<u32>().unwrap()
        })
        .collect::<Vec<_>>();

    values.iter().sum::<u32>()
}