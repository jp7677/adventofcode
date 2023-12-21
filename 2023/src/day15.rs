use crate::util::*;
use std::array;
use std::cmp::Ordering::Less;

#[derive(Debug)]
struct Lens<'a> {
    label: &'a str,
    focal_length: u32,
}

#[derive(Debug)]
enum LensOp {
    Remove,
    Upsert,
}

#[derive(Debug)]
struct Operation<'a> {
    label: &'a str,
    box_nr: usize,
    op: LensOp,
    focal_length: Option<u32>,
}

impl Operation<'_> {
    fn new(label: &str, op: LensOp, focal_length: Option<u32>) -> Operation {
        Operation {
            label,
            box_nr: hash_algorithm(label) as usize,
            op,
            focal_length,
        }
    }
}

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
    let input = read_input(DAYS::Day15);

    let mut boxes: [Vec<Lens>; 256] = array::from_fn(|_| Vec::new());
    let operations = input
        .trim()
        .split(",")
        .map(|s| {
            let op = if s.contains("=") {
                LensOp::Upsert
            } else {
                LensOp::Remove
            };
            let p = s.split_once(['=', '-']).unwrap();
            Operation::new(p.0, op, p.1.parse::<u32>().ok())
        })
        .collect::<Vec<Operation>>();

    operations.iter().for_each(|op| {
        match op.op {
            LensOp::Upsert => {
                if boxes[op.box_nr].iter().any(|l| l.label == op.label) {
                    let index = boxes[op.box_nr]
                        .iter()
                        .position(|l| l.label == op.label)
                        .unwrap();
                    boxes[op.box_nr][index] = Lens {
                        label: op.label,
                        focal_length: op.focal_length.unwrap(),
                    };
                } else {
                    boxes[op.box_nr].push(Lens {
                        label: op.label,
                        focal_length: op.focal_length.unwrap(),
                    });
                }
            }
            LensOp::Remove => {
                let index = boxes[op.box_nr].iter().position(|l| l.label == op.label);
                match index {
                    Some(v) => {
                        boxes[op.box_nr].remove(v);
                    }
                    None => {}
                }
            }
        };
    });

    let power = boxes
        .iter()
        .enumerate()
        .flat_map(|(i, b)| {
            b.iter()
                .enumerate()
                .map(move |(j, l)| (i + 1) * (j + 1) * l.focal_length as usize)
        })
        .sum::<usize>();

    assert_eq!(power, 265345);
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
