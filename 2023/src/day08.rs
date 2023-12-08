use crate::util::*;

struct Element<'a> {
    start: &'a str,
    left: &'a str,
    right: &'a str,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day08);

    let (instructions, map) = get_instructions_and_elements(&input);
    let start = map.iter().find(|e| e.start == "AAA").unwrap();
    let steps = get_steps(start, |e| e == "ZZZ", &instructions, &map);

    assert_eq!(steps, 18673);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day08);

    let (instructions, map) = get_instructions_and_elements(&input);
    let steps = map
        .iter()
        .filter(|e| e.start.ends_with('A'))
        .map(|e| get_steps(e, |e| e.ends_with('Z'), &instructions, &map))
        .collect::<Vec<usize>>();

    let steps = lcm(steps.as_slice());

    assert_eq!(steps, 17972669116327);
}

fn get_steps(
    start: &Element,
    end: fn(start: &str) -> bool,
    instructions: &str,
    map: &Vec<Element>,
) -> usize {
    let mut steps = 0;
    let mut current_instruction = 0;
    let mut current_element = start;
    while !end(current_element.start) {
        current_element = map
            .iter()
            .find(|e| {
                if instructions.chars().nth(current_instruction).unwrap() == 'L' {
                    e.start == current_element.left
                } else {
                    e.start == current_element.right
                }
            })
            .unwrap();
        current_instruction = (current_instruction + 1) % instructions.len();
        steps = steps + 1
    }
    steps
}

fn get_instructions_and_elements(input: &str) -> (&str, Vec<Element>) {
    let instructions = input.lines().nth(0).unwrap();
    let map = input
        .lines()
        .skip(2)
        .map(|line| Element {
            start: &line[0..3],
            left: &line[7..10],
            right: &line[12..15],
        })
        .collect::<Vec<Element>>();

    (instructions, map)
}
