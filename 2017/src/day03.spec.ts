import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 03", () => {
  test("part 1", async () => {
    const input = await readInput("day03-input.txt");
    const number = parseInt(input[0]);

    let step = { base: 1, start: 1 };
    while (step.start < number) {
      step = { base: step.base + 2, start: Math.pow(step.base + 2, 2) };
    }

    const side = Math.sqrt(step.start) - 1;
    const offset = (number - Math.pow(step.base - 2, 2)) % side;
    const x = (step.base - 1) / 2;
    const y = Math.abs(side / 2 - offset);

    expect(x + y).toBe(438);
  });
});
