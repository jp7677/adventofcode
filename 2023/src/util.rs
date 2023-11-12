#![allow(dead_code)]

use std::fs;

pub fn read_input(name: &str) -> String {
    return fs::read_to_string( format!("data/{name}"))
        .expect("Invalid input file name");
}
