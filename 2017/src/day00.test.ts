import { describe, test } from "node:test";
import { equal } from "node:assert";
import { readInput } from "./util.ts";

describe("day 00", () => {
  test("part 1", async () => {
    const input = await readInput("day00-input.txt");

    equal(input[0], "0");
  });
});
