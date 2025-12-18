import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 05", () => {
  async function runInstructions(includingDecrease: boolean) {
    const input = await readInput("day05-input.txt");
    const offsets = input.map((s) => parseInt(s, 10));

    let steps = 0;
    let index = 0;
    while (index >= 0 && index < offsets.length) {
      steps++;
      const jump = offsets[index];
      offsets[index] = includingDecrease && jump >= 3 ? offsets[index] - 1 : offsets[index] + 1;
      index += jump;
    }
    return steps;
  }

  test("part 1", async () => {
    const steps = await runInstructions(false);

    expect(steps).toBe(378980);
  });

  test("part 2", async () => {
    const steps = await runInstructions(true);

    expect(steps).toBe(26889114);
  });
});
