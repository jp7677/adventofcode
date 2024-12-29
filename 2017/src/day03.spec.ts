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

  enum Direction {
    NORTH = 1,
    EAST,
    SOUTH,
    WEST,
  }

  interface Coord {
    x: number;
    y: number;
  }

  test("part 2", async () => {
    const input = await readInput("day03-input.txt");
    const number = parseInt(input[0]);

    const seq = generateStressSteps();
    let step;
    do {
      step = seq.next();
    } while (!step.done && step.value < number);

    expect(step.value).toBe(266330);
  });

  function* generateStressSteps() {
    const coords: Map<string, number> = new Map<string, number>();
    coords.set(JSON.stringify({ x: 0, y: 0 }), 1);
    coords.set(JSON.stringify({ x: 1, y: 0 }), 1);

    let current = { coord: { x: 1, y: 0 }, direction: Direction.NORTH };
    while (true) {
      const next = move(current, coords);
      const sum = sumOfAdjacent(coords, next.coord);
      coords.set(JSON.stringify(next.coord), sum);
      current = next;
      yield sum;
    }
  }

  function move(
    current: {
      coord: Coord;
      direction: Direction;
    },
    coords: Map<string, number>,
  ) {
    switch (current.direction) {
      case Direction.NORTH:
        if (
          coords.has(
            JSON.stringify({ x: current.coord.x - 1, y: current.coord.y }),
          )
        ) {
          return {
            coord: { x: current.coord.x, y: current.coord.y - 1 },
            direction: Direction.NORTH,
          };
        } else {
          return {
            coord: { x: current.coord.x - 1, y: current.coord.y },
            direction: Direction.WEST,
          };
        }
      case Direction.WEST:
        if (
          coords.has(
            JSON.stringify({ x: current.coord.x, y: current.coord.y + 1 }),
          )
        ) {
          return {
            coord: { x: current.coord.x - 1, y: current.coord.y },
            direction: Direction.WEST,
          };
        } else {
          return {
            coord: { x: current.coord.x, y: current.coord.y + 1 },
            direction: Direction.SOUTH,
          };
        }
      case Direction.SOUTH:
        if (
          coords.has(
            JSON.stringify({ x: current.coord.x + 1, y: current.coord.y }),
          )
        ) {
          return {
            coord: { x: current.coord.x, y: current.coord.y + 1 },
            direction: Direction.SOUTH,
          };
        } else {
          return {
            coord: { x: current.coord.x + 1, y: current.coord.y },
            direction: Direction.EAST,
          };
        }
      case Direction.EAST:
        if (
          coords.has(
            JSON.stringify({ x: current.coord.x, y: current.coord.y - 1 }),
          )
        ) {
          return {
            coord: { x: current.coord.x + 1, y: current.coord.y },
            direction: Direction.EAST,
          };
        } else {
          return {
            coord: { x: current.coord.x, y: current.coord.y - 1 },
            direction: Direction.NORTH,
          };
        }
    }
  }

  const sumOfAdjacent = (coords: Map<string, number>, coord: Coord) =>
    [
      { x: -1, y: -1 },
      { x: 0, y: -1 },
      { x: 1, y: -1 },
      { x: -1, y: 0 },
      { x: 1, y: 0 },
      { x: -1, y: 1 },
      { x: 0, y: 1 },
      { x: 1, y: 1 },
    ].reduce(
      (acc, it) =>
        acc +
        (coords.get(JSON.stringify({ x: coord.x + it.x, y: coord.y + it.y })) ||
          0),
      0,
    );
});
