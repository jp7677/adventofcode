import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

enum Op {
  INC = "INC",
  DEC = "DEC",
}

enum Cond {
  GREATER_THAN = "GREATER_THAN",
  GREATER_THAN_OR_EQUAL = "GREATER_THAN_OR_EQUAL",
  EQUALS = "EQUALS",
  NOT_EQUALS = "NOT_EQUALS",
  LESS_THAN = "LESS_THAN",
  LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS",
}

class Operation {
  readonly register: string;
  readonly op: Op;
  readonly value: number;

  constructor(register: string, op: string, value: string) {
    this.register = register;
    this.op = this.parseOp(op);
    this.value = parseInt(value);
  }

  private parseOp(value: string): Op {
    switch (value) {
      case "inc":
        return Op.INC;
      case "dec":
        return Op.DEC;
    }
    throw new Error();
  }

  run(registers: Map<string, number>) {
    switch (this.op) {
      case Op.INC:
        return registers.set(this.register, getValue(registers, this.register) + this.value);
      case Op.DEC:
        return registers.set(this.register, getValue(registers, this.register) - this.value);
    }
  }
}

class Condition {
  readonly register: string;
  readonly cond: Cond;
  readonly value: number;

  constructor(register: string, cond: string, value: string) {
    this.register = register;
    this.cond = this.parseCond(cond);
    this.value = parseInt(value);
  }

  parseCond(value: string): Cond {
    switch (value) {
      case ">":
        return Cond.GREATER_THAN;
      case ">=":
        return Cond.GREATER_THAN_OR_EQUAL;
      case "==":
        return Cond.EQUALS;
      case "!=":
        return Cond.NOT_EQUALS;
      case "<":
        return Cond.LESS_THAN;
      case "<=":
        return Cond.LESS_THAN_OR_EQUALS;
    }
    throw new Error();
  }

  evaluate(registers: Map<string, number>): boolean {
    switch (this.cond) {
      case Cond.GREATER_THAN:
        return getValue(registers, this.register) > this.value;
      case Cond.GREATER_THAN_OR_EQUAL:
        return getValue(registers, this.register) >= this.value;
      case Cond.EQUALS:
        return getValue(registers, this.register) == this.value;
      case Cond.NOT_EQUALS:
        return getValue(registers, this.register) != this.value;
      case Cond.LESS_THAN:
        return getValue(registers, this.register) < this.value;
      case Cond.LESS_THAN_OR_EQUALS:
        return getValue(registers, this.register) <= this.value;
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

    expect(largest[largest.length - 1]).toBe(6061);
    expect(largest.max()).toBe(6696);
  });
});
