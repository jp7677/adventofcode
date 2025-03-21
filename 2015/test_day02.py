import re
from util import read_input


def test_part01():
    input_data = read_input("day02-input.txt").split('\n')
    presents = map(lambda it: list(map(int, re.findall(r'(\d+)', it))) , input_data)

    square_feets = map(lambda it: [it[0]*it[1], it[0]*it[2], it[1]*it[2]], presents)
    square_feets = map(lambda it: 2*it[0] + 2*it[1] + 2*it[2] + min(it), square_feets)
    square_feets = sum(square_feets)

    assert square_feets == 1588178


def test_part02():
    input_data = read_input("day02-input.txt").split('\n')
    presents = map(lambda it: list(map(int, re.findall(r'(\d+)', it))) , input_data)

    square_feets = map(lambda it:
        2 * min([it[0]+it[1], it[0]+it[2], it[1]+it[2]]) + (it[0] * it[1] * it[2]), presents)
    square_feets = sum(square_feets)

    assert square_feets == 3783758
