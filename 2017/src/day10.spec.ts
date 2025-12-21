import { describe, expect, test } from "@jest/globals";
import { readInput, repeat } from "./util";

function hash(input: number[], rounds: number): number[] {
  const forEachIndex = (start: number, end: number, size: number, fn: (i: number) => void) => {
    let i = start;
    do {
      fn(i);
      i = (i + 1) % size;
    } while (i !== (end + 1) % size);
  };

  const size = 256;
  const sparseHash = new Array(size);
  for (let i = 0; i < size; i++) sparseHash[i] = i;

  let position = 0;
  let skipSize = 0;
  repeat(rounds, () => {
    input.forEach((length) => {
      if (length > 0) {
        const end = (position + length - 1) % size;
        const selected = Array(length);
        forEachIndex(position, end, size, (i) => selected.push(sparseHash[i]));
        forEachIndex(position, end, size, (i) => (sparseHash[i] = selected.pop()));
      }
      position = (position + length + skipSize) % size;
      skipSize += 1;
    });
  });
  return sparseHash;
}

describe("day 10", () => {
  test("part 1", async () => {
    const input = await readInput("day10-input.txt");
    const lengths = input[0].split(",").map((c) => parseInt(c));

    const sparseHash = hash(lengths, 1);

    expect(sparseHash[0] * sparseHash[1]).toBe(11413);
  });

  test("part 2", async () => {
    const input = await readInput("day10-input.txt");
    const codes = input[0].split("").map((_, i) => input[0].charCodeAt(i));
    codes.push(17, 31, 73, 47, 23);

    const sparseHash = hash(codes, 64);
    const denseHash = sparseHash.reduce((acc, v, i) => {
      acc[Math.floor(i / 16)] ^= v;
      return acc;
    }, new Array<number>(16));
    const knotHash = denseHash.reduce((acc, v) => acc.concat(v.toString(16).padStart(2, "0")), "");

    expect(knotHash).toBe("7adfd64c2a03a4968cf708d1b7fd418d");
  });
});
