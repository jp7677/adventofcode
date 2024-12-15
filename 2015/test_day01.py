from util import read_input


def test_part01():
    input_data = read_input("day01-input.txt")

    up = input_data.count(')')
    down = input_data.count('(')
    floor = abs(up - down)

    assert floor == 138
