import { describe, test } from "node:test";
import { equal } from "node:assert";
import { readInput } from "./util.ts";

describe("day 04", () => {
  test("part 1", async () => {
    const input = await readInput("day04-input.txt");

    const passphrases = input.map((p) => p.split(" "));
    const valid = passphrases.reduce((acc, it) => acc + (it.length == new Set(it).size ? 1 : 0), 0);

    equal(valid, 386);
  });

  test("part 2", async () => {
    const input = await readInput("day04-input.txt");

    const passphrases = input.map((p) => p.split(" ")).map((p) => p.map((p) => p.split("").sort().join("")));

    const valid = passphrases.reduce((acc, it) => acc + (it.length == new Set(it).size ? 1 : 0), 0);

    equal(valid, 208);
  });
});
