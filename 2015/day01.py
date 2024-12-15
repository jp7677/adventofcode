import unittest

from util import read_input


class Day00(unittest.TestCase):

    def test_part01(self):
        input_data = read_input("day01-input.txt")

        up = input_data.count(')')
        down = input_data.count('(')
        floor = abs(up - down)

        self.assertEqual(floor, 138)

if __name__ == '__main__':
    unittest.main()
