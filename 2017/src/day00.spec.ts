import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 00", () => {
  test("part 1", async () => {
    const input = await readInput("day00-input.txt");

    expect(input[0]).toBe("0");
  });
});
