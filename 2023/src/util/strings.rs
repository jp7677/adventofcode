#[inline(always)]
pub fn char_at(str: &str, index: usize) -> char {
    // Not a good idea for all kinds of strings.
    *(&str[index..index + 1].chars().nth(0).unwrap())
}
