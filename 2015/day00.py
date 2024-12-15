import unittest

from util import read_input


class Day01(unittest.TestCase):

    def test_part01(self):
        input_data = read_input("day00-input.txt")

        self.assertEqual(input_data, '0')

if __name__ == '__main__':
    unittest.main()
