from operator import contains

from util import read_input


def test_part01():
    input_data = read_input("day05-input.txt").split('\n')

    nice = filter(is_nice, input_data)
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


def is_nice(line):
    return at_least_3_vowels(line) and one_letter_twice(line) and no_forbidden(line)
