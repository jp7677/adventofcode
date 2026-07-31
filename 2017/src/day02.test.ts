import { describe, test } from "node:test";
import { equal } from "node:assert";
import { readInput } from "./util.ts";

describe("day 02", () => {
  test("part 1", async () => {
    const input = await readInput("day02-input.txt");
    const spreadsheet = input.map(
      // eslint-disable-next-line no-control-regex
      (line) => line.split(RegExp("\t")).map((c) => parseInt(c)),
    );

    const checksum = spreadsheet.map((row) => row.max() - row.min()).sum();

    equal(checksum, 45158);
  });

  test("part 2", async () => {
    const input = await readInput("day02-input.txt");
    const spreadsheet = input.map(
      // eslint-disable-next-line no-control-regex
      (line) => line.split(RegExp("\t")).map((c) => parseInt(c)),
    );

    const checksum = spreadsheet
      .map((row) => {
        let n1 = 0;
        let n2 = 0;
        row.forEach((a) => {
          row.forEach((b) => {
            if (a != b && a % b == 0) {
              n1 = a;
              n2 = b;
            }
          });
        });

        return n1 > n2 ? n1 / n2 : n2 / n1;
      })
      .sum();

    equal(checksum, 294);
  });
});
