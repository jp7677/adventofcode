// From https://github.com/TheAlgorithms/Rust/blob/master/src/math/lcm_of_n_numbers.rs

pub fn lcm(nums: &[usize]) -> usize {
    if nums.len() == 1 {
        return nums[0];
    }
    let a = nums[0];
    let b = lcm(&nums[1..]);
    a * b / gcd_of_two_numbers(a, b)
}

fn gcd_of_two_numbers(a: usize, b: usize) -> usize {
    if b == 0 {
        return a;
    }
    gcd_of_two_numbers(b, a % b)
}

pub fn abs(a: u64, b: u64) -> u64 {
    if a > b {
        a - b
    } else {
        b - a
    }
}
