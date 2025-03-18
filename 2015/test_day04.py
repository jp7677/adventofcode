import sys
from hashlib import md5
from util import read_input


def test_part01():
    input_data = read_input("day04-input.txt")

    secret = find_secret(input_data, "00000")

    assert secret == 346386

def test_part02():
    input_data = read_input("day04-input.txt")

    secret = find_secret(input_data, "000000")

    assert secret == 9958218

def find_secret(input_data, zeroes):
    secret = 0

    for i in range(sys.maxsize):
        md5_input = input_data + str(i)
        x = md5(md5_input.encode()).digest().hex()
        if x.startswith(zeroes):
            secret = i
            break

    return secret
