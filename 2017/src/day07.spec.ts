import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

interface Program {
  name: string;
  weight: number;
  totalWeight: number;
  programs: Set<string>;
}

async function readPrograms() {
  const input = await readInput("day07-input.txt");
  return input.map((line) => {
    const parts = line.match(/([a-z]+) \((\d+)\)( -> (.+))?/);
    if (!parts) throw new Error();
    return {
      name: parts[1],
      weight: parseInt(parts[2]),
      totalWeight: 0,
      programs: new Set(parts[4]?.split(", ") ?? []),
    } as Program;
  });
}

function findBottom(programs: Program[], name: string) {
  const current = programs.find((x) => x.programs.has(name));
  if (!current) return name;

  return findBottom(programs, current.name);
}

function calculaleTotalWeights(programs: Program[], name: string) {
  const current = programs.find((x) => x.name == name);
  if (!current) throw new Error();

  if (current?.programs.size > 0)
    current.totalWeight = current.programs.values().reduce((acc, x) => {
      return acc + calculaleTotalWeights(programs, x);
    }, current.weight);
  else current.totalWeight = current.weight;

  return current.totalWeight;
}

describe("day 07", () => {
  test("part 1", async () => {
    const programs = await readPrograms();

    const bottom = findBottom(programs, programs[0].name);

    expect(bottom).toBe("hmvwl");
  });

  test("part 2", async () => {
    const programs = await readPrograms();
    const bottom = findBottom(programs, programs[0].name);
    calculaleTotalWeights(programs, bottom);

    const unbalanced = programs
      .find((program) => {
        const x = program.programs
          .values()
          .map((name) => {
            return programs.find((y) => y.name == name)?.totalWeight;
          })
          .toArray();
        return x.length > 0 && x.min() != x.max();
      })
      ?.programs.values()
      .map((name) => programs.find((y) => y.name == name))
      .toArray();

    if (!unbalanced) throw new Error();

    const correct = unbalanced.find(
      (program) => unbalanced.filter((p) => p?.totalWeight == program?.totalWeight).length > 1,
    );
    const wrong = unbalanced.find(
      (program) => unbalanced.filter((p) => p?.totalWeight == program?.totalWeight).length == 1,
    );

    if (!correct || !wrong) throw new Error();

    const weight = wrong.weight - (wrong.totalWeight - correct.totalWeight);

    expect(weight).toBe(1853);
  });
});
