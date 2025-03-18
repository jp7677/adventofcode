from util import read_input


def test_part01():
    directions = read_input("day03-input.txt")
    path = walk(directions)

    assert len(set(path)) == 2592

def test_part02():
    directions = read_input("day03-input.txt")

    santa_direction = ''
    for i, c in enumerate(directions):
        if i % 2 == 0:
            santa_direction += c

    robot_direction = ''
    for i, c in enumerate(directions):
        if i % 2 != 0:
            robot_direction += c

    santa_path = walk(santa_direction)
    robot_path = walk(robot_direction)

    assert len(set(santa_path + robot_path)) == 2360


def walk(directions):
    pos = [(0, 0)]
    for i, c in enumerate(directions):
        if c == '>': pos.append((pos[i][0] + 1, pos[i][1]))
        if c == 'v': pos.append((pos[i][0], pos[i][1] + 1))
        if c == '<': pos.append((pos[i][0] - 1, pos[i][1]))
        if c == '^': pos.append((pos[i][0], pos[i][1] - 1))
    return pos
