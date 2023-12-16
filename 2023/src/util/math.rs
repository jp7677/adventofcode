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

pub fn choose(n: usize, k: usize) -> usize {
    if k == 0 {
        return 1;
    }

    let mut binomial_coefficient = 1;
    for i in 1..k + 1 {
        binomial_coefficient = binomial_coefficient * (n - (k - i));
        binomial_coefficient = binomial_coefficient / i;
    }
    binomial_coefficient
}

// aka https://brilliant.org/wiki/integer-equations-star-and-bars/
// also https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)
pub fn balls_and_urns(n: usize, k: usize) -> Vec<Vec<usize>> {
    let mut acc = Vec::new();
    balls_and_urns1(&mut acc, &Vec::new(), n, 1, k);
    acc
}

fn balls_and_urns1(
    acc: &mut Vec<Vec<usize>>,
    v: &Vec<usize>,
    n: usize,
    depth: usize,
    max_depth: usize,
) {
    let sum = v.iter().sum::<usize>();
    let remain = if sum <= n { 0 } else { n - sum };

    if depth == max_depth {
        let mut v1 = v.clone();
        v1.push(n - sum);
        acc.push(v1.clone());
        return;
    } else if remain >= (n - sum + 1) {
        let mut v1 = v.clone();
        v1.push(0);
        balls_and_urns1(acc, &mut v1, n, depth + 1, max_depth);
    } else {
        for i in 0..(n - sum + 1) {
            let mut v1 = v.clone();
            v1.push(i);
            balls_and_urns1(acc, &mut v1, n, depth + 1, max_depth);
        }
    }
}
