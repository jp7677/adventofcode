import { describe, test } from "node:test";
import { equal } from "node:assert";
import { readInput } from "./util.ts";

type Op = "inc" | "dec";
type Cond = ">" | ">=" | "==" | "!=" | "<" | "<=";

class Operation {
  readonly register: string;
  readonly op: Op;
  readonly value: number;

  constructor(register: string, op: string, value: string) {
    this.register = register;
    this.op = op as Op;
    this.value = parseInt(value);
  }

  run(registers: Map<string, number>) {
    switch (this.op) {
      case "inc":
        return registers.set(this.register, getValue(registers, this.register) + this.value);
      case "dec":
        return registers.set(this.register, getValue(registers, this.register) - this.value);
      default:
        throw new RangeError(this.op);
    }
  }
}

class Condition {
  readonly register: string;
  readonly cond: Cond;
  readonly value: number;

  constructor(register: string, cond: string, value: string) {
    this.register = register;
    this.cond = cond as Cond;
    this.value = parseInt(value);
  }

  evaluate(registers: Map<string, number>): boolean {
    switch (this.cond) {
      case ">":
        return getValue(registers, this.register) > this.value;
      case ">=":
        return getValue(registers, this.register) >= this.value;
      case "==":
        return getValue(registers, this.register) == this.value;
      case "!=":
        return getValue(registers, this.register) != this.value;
      case "<":
        return getValue(registers, this.register) < this.value;
      case "<=":
        return getValue(registers, this.register) <= this.value;
      default:
        throw new RangeError(this.cond);
    }
  }
}

class Instruction {
  readonly operation: Operation;
  readonly condition: Condition;

  constructor(line: string) {
    const parts = line.split(" ");
    this.operation = new Operation(parts[0], parts[1], parts[2]);
    this.condition = new Condition(parts[4], parts[5], parts[6]);
  }

  execute(registers: Map<string, number>) {
    if (this.condition.evaluate(registers)) this.operation.run(registers);
  }
}

function getValue(registers: Map<string, number>, key: string) {
  const value = registers.get(key);
  if (value === undefined) throw new Error();
  return value;
}

describe("day 08", () => {
  test("part 1 and 2", async () => {
    const input = await readInput("day08-input.txt");
    const instructions = input.map((line) => new Instruction(line));

    const registers = new Map<string, number>(instructions.map((ins) => [ins.operation.register, 0]));
    const largest = instructions.map((instruction) => {
      instruction.execute(registers);
      return registers.values().toArray().max();
    });

    equal(largest[largest.length - 1], 6061);
    equal(largest.max(), 6696);
  });
});
