import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 04", () => {
  test("part 1", async () => {
    const input = await readInput("day04-input.txt");

    const passphrases = input.map((p) => p.split(" "));
    const valid = passphrases.reduce((acc, it) => acc + (it.length == new Set(it).size ? 1 : 0), 0);

    expect(valid).toBe(386);
  });

  test("part 2", async () => {
    const input = await readInput("day04-input.txt");

    const passphrases = input.map((p) => p.split(" ")).map((p) => p.map((p) => p.split("").sort().join("")));

    const valid = passphrases.reduce((acc, it) => acc + (it.length == new Set(it).size ? 1 : 0), 0);

    expect(valid).toBe(208);
  });
});
