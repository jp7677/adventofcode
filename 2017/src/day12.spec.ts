import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

async function readPrograms() {
  const input = await readInput("day12-input.txt");
  return input.reduce((acc, line) => {
    const parts = line.split(" <-> ");
    const id = parseInt(parts[0]);
    const connectedIds = parts[1].split(", ").map((n) => parseInt(n));
    acc.set(id, connectedIds);
    return acc;
  }, new Map<number, number[]>());
}

function discoverGroup(programs: Map<number, number[]>, id: number) {
  const group = new Set([id]);
  let discoveredIds = [id];
  while (discoveredIds.length !== 0) {
    discoveredIds = discoveredIds.flatMap((id) => programs.get(id) ?? []).filter((id) => !group.has(id));
    discoveredIds.forEach((id) => group.add(id));
  }
  return group;
}

describe("day 12", () => {
  test("part 1", async () => {
    const programs = await readPrograms();

    const group = discoverGroup(programs, 0);

    expect(group.size).toBe(115);
  });

  test("part 2", async () => {
    const programs = await readPrograms();

    const groups = programs.keys().reduce((acc, id) => {
      const discoveredIds = acc.flatMap((ids) => ids.values().toArray()).toSet();
      if (!discoveredIds.has(id)) acc.push(discoverGroup(programs, id));
      return acc;
    }, new Array<Set<number>>());

    expect(groups.length).toBe(221);
  });
});
