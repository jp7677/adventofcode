use std::collections::HashMap;

#[inline(always)]
pub fn ansi_char_at(str: &str, index: usize) -> char {
    // Not a good idea for all kinds of strings.
    *(&str[index..index + 1].chars().nth(0).unwrap())
}

pub fn char_count(s: &str) -> HashMap<char, u32> {
    s.chars().fold(HashMap::new(), |mut map, c| {
        *map.entry(c).or_insert(0) += 1;
        map
    })
}
