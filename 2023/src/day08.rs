use crate::util::*;
use std::collections::HashMap;

struct Element<'a> {
    left: &'a str,
    right: &'a str,
}

#[test]
fn part01() {
    let input = read_input(DAYS::Day08);

    let (instructions, map) = get_instructions_and_elements(&input);
    let steps = get_steps("AAA", |e| e == "ZZZ", &instructions, &map);

    assert_eq!(steps, 18673);
}

#[test]
fn part02() {
    let input = read_input(DAYS::Day08);

    let (instructions, map) = get_instructions_and_elements(&input);
    let steps = map
        .iter()
        .filter(|(k, _)| k.ends_with('A'))
        .map(|(k, _)| get_steps(k, |e| e.ends_with('Z'), &instructions, &map))
        .collect::<Vec<usize>>();

    let steps = lcm(steps.as_slice());

    assert_eq!(steps, 17972669116327);
}

fn get_steps(
    start_element: &str,
    should_end: fn(start: &str) -> bool,
    instructions: &str,
    map: &HashMap<&str, Element>,
) -> usize {
    let mut steps = 0;
    let mut instruction_index = 0;
    let mut current_element = start_element;
    while !should_end(current_element) {
        let element = map.get(current_element).unwrap();
        current_element = if ansi_char_at(instructions, instruction_index) == 'L' {
            element.left
        } else {
            element.right
        };

        instruction_index = (instruction_index + 1) % instructions.len();
        steps = steps + 1
    }
    steps
}

fn get_instructions_and_elements(input: &str) -> (&str, HashMap<&str, Element<'_>>) {
    let instructions = input.lines().nth(0).unwrap();
    let map = input
        .lines()
        .skip(2)
        .map(|line| {
            (
                &line[0..3],
                Element {
                    left: &line[7..10],
                    right: &line[12..15],
                },
            )
        })
        .collect::<HashMap<&str, Element>>();

    (instructions, map)
}
