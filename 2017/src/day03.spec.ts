import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 03", () => {
  test("part 1", async () => {
    const input = await readInput("day03-input.txt");
    const number = parseInt(input[0]);

    const steps = Array.from(generateSteps(number));
    const prev = steps[steps.length - 2];
    const last = steps[steps.length - 1];

    const a = last.side / 2;
    const b = Math.abs(last.side / 2 - ((number - prev.start) % last.side));

    expect(a + b).toBe(438);
  });

  function* generateSteps(number: number) {
    let step = { base: 1, start: 1 };
    while (step.start < number) {
      step = { base: step.base + 2, start: Math.pow(step.base + 2, 2) };
      yield { side: step.base - 1, start: step.start };
    }
  }
});
