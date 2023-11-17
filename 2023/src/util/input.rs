use super::days::DAYS;
use std::fs;

pub fn read_input(day: DAYS) -> String {
    let day = format!("day{:02}-input.txt", day as u8);
    fs::read_to_string(format!("data/{day}"))
        .expect(format!("Invalid input file name: {day}").as_str())
}
