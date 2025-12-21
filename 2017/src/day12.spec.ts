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
  let group = new Set<number>([id]);
  let discoveredIds = new Set<number>([id]);
  do {
    discoveredIds = discoveredIds
      .values()
      .flatMap((id) => {
        return programs.filter((p) => p.id === id).flatMap((v) => v.connectedIds.values().toArray());
      })
      .filter((id) => !group.has(id))
      .toArray()
      .toSet();

    group = group.union(discoveredIds);
  } while (discoveredIds.size !== 0);
  return group;
}

describe("day 12", () => {
  test("part 1", async () => {
    const programs = await readGroups();

    const zeroGroup = discoverGroup(programs, 0);

    expect(zeroGroup.size).toBe(115);
  });

  test("part 2", async () => {
    const programs = await readGroups();

    const groups = programs.reduce((acc, pipe) => {
      const discoveredIds = new Set(acc.flatMap((p) => p.values().toArray()));
      if (!discoveredIds.has(pipe.id)) acc.push(discoverGroup(programs, pipe.id));
      return acc;
    }, new Array<Set<number>>());

    expect(groups.length).toBe(221);
  });
});
