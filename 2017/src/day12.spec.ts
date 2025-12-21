import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

interface Pipe {
  id: number;
  connectedIds: Set<number>;
}

async function readGroups() {
  const input = await readInput("day12-input.txt");
  return input.map((line) => {
    const parts = line.split(" <-> ");
    const id = parseInt(parts[0]);
    const connectedIds = parts[1].split(", ").map((n) => parseInt(n));
    return { id: id, connectedIds: new Set(connectedIds) } as Pipe;
  });
}

function discoverGroup(programs: Pipe[], id: number) {
  const group = new Set<number>([id]);
  let discoveredIds = [id];
  while (discoveredIds.length !== 0) {
    discoveredIds = discoveredIds
      .flatMap((id) => programs.filter((p) => p.id === id).flatMap((v) => v.connectedIds.values().toArray()))
      .filter((id) => !group.has(id));

    discoveredIds.forEach((id) => group.add(id));
  }
  return group;
}

describe("day 12", () => {
  test("part 1", async () => {
    const programs = await readGroups();

    const group = discoverGroup(programs, 0);

    expect(group.size).toBe(115);
  });

  test("part 2", async () => {
    const programs = await readGroups();

    const groups = programs.reduce((acc, pipe) => {
      const discoveredIds = acc.flatMap((ids) => ids.values().toArray()).toSet();
      if (!discoveredIds.has(pipe.id)) acc.push(discoverGroup(programs, pipe.id));
      return acc;
    }, new Array<Set<number>>());

    expect(groups.length).toBe(221);
  });
});
