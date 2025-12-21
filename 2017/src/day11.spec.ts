import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

interface Coord {
  x: number;
  y: number;
}

async function walk() {
  const input = await readInput("day11-input.txt");
  const directions = input[0].split(",");

  return directions.reduce(
    (acc, v) => {
      const last = acc[acc.length - 1];
      switch (v) {
        case "n": {
          acc.push({ x: last.x, y: last.y + 1 });
          break;
        }
        case "ne": {
          acc.push({ x: last.x + 1, y: last.y + 0.5 });
          break;
        }
        case "se": {
          acc.push({ x: last.x + 1, y: last.y - 0.5 });
          break;
        }
        case "s": {
          acc.push({ x: last.x, y: last.y - 1 });
          break;
        }
        case "sw": {
          acc.push({ x: last.x - 1, y: last.y - 0.5 });
          break;
        }
        case "nw": {
          acc.push({ x: last.x - 1, y: last.y + 0.5 });
          break;
        }
      }
      return acc;
    },
    [{ x: 0, y: 0 } as Coord],
  );
}

function distance(location: Coord) {
  return Math.abs(location.x) + Math.max(Math.abs(location.y) - Math.abs(location.x) / 2, 0);
}

describe("day 00", () => {
  test("part 1", async () => {
    const path = await walk();

    const lastDistance = distance(path[path.length - 1]);

    expect(lastDistance).toBe(805);
  });

  test("part 2", async () => {
    const path = await walk();

    const maxDistance = path.map((location) => distance(location)).max();

    expect(maxDistance).toBe(1535);
  });
});
