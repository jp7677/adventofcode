import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

describe("day 09", () => {
  test("part 1 and 2", async () => {
    const input = await readInput("day09-input.txt");
    let stream = input[0];

    let p = stream.indexOf("!");
    while (p !== -1) {
      stream = stream.slice(0, p) + stream.slice(p + 2);
      p = stream.indexOf("!");
    }

    let removed = 0;
    let cleaned = stream;
    p = cleaned.indexOf("<");
    while (p !== -1) {
      cleaned = cleaned.slice(0, p) + cleaned.slice(cleaned.indexOf(">") + 1);
      p = cleaned.indexOf("<");
      removed++;
    }

    let depth = 0;
    let score = 0;
    cleaned.split("").forEach((c) => {
      if (c === "{") depth++;
      if (c === "}") {
        score += depth;
        depth--;
      }
    });

    expect(score).toBe(10800);
    expect(stream.length - (cleaned.length + removed * 2)).toBe(4522);
  });
});
