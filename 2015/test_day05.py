from operator import contains

from util import read_input


def test_part01():
    input_data = read_input("day05-input.txt").split('\n')

    nice = filter(
        lambda it: at_least_3_vowels(it) and one_letter_twice(it) and no_forbidden(it),
        input_data
    )
    nice = list(nice)

    assert len(nice) == 238


vowels = ["a", "e", "i", "o", "u"]

def at_least_3_vowels(line):
    return len(list(filter(lambda it: contains(vowels, it), line))) >= 3


def one_letter_twice(line: str):
    firsts = line[1:]
    seconds = line[:-1]
    for first, second in zip(firsts, seconds):
        if first == second:
            return True

    return False


forbidden = ["ab", "cd", "pq", "xy"]

def no_forbidden(line: str):
    for f in forbidden:
        if line.find(f) != -1:
            return False

    return True


def test_part02():
    input_data = read_input("day05-input.txt").split('\n')

    nice = filter(
        lambda it: pair_of_any_two_letter(it) and one_letter_twice_with_one_between(it),
        input_data
    )
    nice = list(nice)

    assert len(nice) == 69


def pair_of_any_two_letter(line: str):
    firsts = line[1:]
    seconds = line[:-1]
    for first, second in zip(firsts, seconds):
        s = second + first
        li = line.find(s)
        ri = line.rfind(s)
        if ri > li + 1:
            return True

    return False


def one_letter_twice_with_one_between(line: str):
    firsts = line[2:]
    seconds = line[:-2]
    for first, second in zip(firsts, seconds):
        if first == second:
            return True

    return False
