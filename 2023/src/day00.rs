#[cfg(test)]
mod test {
    use crate::util;

    #[test]
    fn part01() {
        let input = util::read_input("day00-input.txt");

        assert_eq!(input.lines().count(), 1);
        assert_eq!(input, "0");
    }
}
