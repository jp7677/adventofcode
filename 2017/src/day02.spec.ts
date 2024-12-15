import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 02", () => {
  test("part 1", async () => {
    const input = await readInput("day02-input.txt");

    const spreadsheet = input.map(
      (line) => line.split(RegExp("\t")).map((c) => parseInt(c)), // eslint-disable-line no-control-regex
    );

    const diff = spreadsheet.map((row) => {
      const largest = row.reduce((acc, it) => {
        if (it > acc) return it;
        else return acc;
      }, 0);
      const smallest = row.reduce((acc, it) => {
        if (it < acc) return it;
        else return acc;
      });
      return largest - smallest;
    });

    const checksum = diff.reduce((acc, it) => acc + it);

    expect(checksum).toBe(45158);
  });
});
