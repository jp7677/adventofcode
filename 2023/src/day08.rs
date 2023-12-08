use crate::util::*;

struct Element<'a> {
    start: &'a str,
    left: &'a str,
    right: &'a str
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day08);

    let instructions = input.lines().nth(0).unwrap();
    let map = input.lines().skip(2).map(|line|{
        Element {
            start: &line[0..3],
            left: &line[7..10],
            right: &line[12..15]
        }
    }).collect::<Vec<Element>>();

    let mut steps = 0;
    let mut current_instruction = 0;
    let mut current_element = map.iter().find(|e| e.start == "AAA");
    while current_element.unwrap().start != "ZZZ" {
        current_element = map.iter().find(|e|
            if instructions.chars().nth(current_instruction).unwrap() == 'L' {
                e.start == current_element.unwrap().left
            } else {
                e.start == current_element.unwrap().right
            }
        );
        current_instruction = (current_instruction + 1) % instructions.len();
        steps = steps + 1
    }

    assert_eq!(steps, 18673);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day00);

    assert_eq!(input.lines().count(), 1);
}
