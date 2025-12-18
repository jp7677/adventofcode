import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

function reallocate(memory: number[]) {
  const highest = memory.max();
  const highestIndex = memory.indexOf(highest);
  memory[highestIndex] = 0;
  for (let i = 0; i < highest; i++) memory[(highestIndex + i + 1) % memory.length]++;

  return memory.join("-");
}

describe("day 06", () => {
  test("part 1 and 2", async () => {
    const input = await readInput("day06-input.txt");

    const memory = input[0].split("\t").map((c) => parseInt(c));

    const cycles: string[] = [];
    const unique = new Set(cycles);
    while (unique.size == cycles.length) {
      const reallocated = reallocate(memory);
      cycles.push(reallocated);
      unique.add(reallocated);
    }

    const start = cycles.indexOf(cycles[cycles.length - 1]);

    expect(cycles.length).toBe(7864);
    expect(cycles.length - start - 1).toBe(1695);
  });
});
