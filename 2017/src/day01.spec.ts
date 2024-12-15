import {describe, expect, test} from '@jest/globals';
import {readInput} from "./util";


describe('day 01', () => {

  test('part 1', async () => {
    const input = await readInput('day01-input.txt')
    const captcha = input[0].split('').map(c => parseInt(c))

    const sum = [...captcha, captcha[0]]
      .reduce((acc, it, idx, array) => {
        if (it == array[idx-1])
          return acc + it
        else
          return acc
      }, 0)

    expect(sum).toBe(1029)
  });

});
