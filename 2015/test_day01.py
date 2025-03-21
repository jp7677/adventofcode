from util import read_input


def test_part01():
    directions = read_input("day01-input.txt")

    floor = abs(directions.count(')') - directions.count('('))

    assert floor == 138


def test_part02():
    directions = read_input("day01-input.txt")

    position = 0
    floor = 0
    for i, f in enumerate(directions):
        if f == '(': floor += 1
        else: floor -= 1

        if floor == -1:
            position = i + 1
            break

    assert position == 1771
