#[cfg(test)]
mod test {

    #[test]
    fn part01() {
        let input = crate::util::read_input("day00-input.txt");

        assert_eq!(input.lines().count(), 1);
        assert_eq!(input, "0");
    }
}
