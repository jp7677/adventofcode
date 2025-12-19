import { describe, expect, test } from "@jest/globals";
import { readInput } from "./util";

enum Op {
  INC = "INC",
  DEC = "DEC",
}

function parseOp(value: string): Op {
  switch (value) {
    case "inc":
      return Op.INC;
    case "dec":
      return Op.DEC;
  }
  throw new Error();
}

enum Cond {
  GREATER_THAN = "GREATER_THAN",
  GREATER_THAN_OR_EQUAL = "GREATER_THAN_OR_EQUAL",
  EQUALS = "EQUALS",
  NOT_EQUALS = "NOT_EQUALS",
  LESS_THAN = "LESS_THAN",
  LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS",
}

interface Operation {
  register: string;
  op: Op;
  value: number;
}

interface Condition {
  register: string;
  cond: Cond;
  value: number;
}

function parseCond(value: string): Cond {
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

interface Instruction {
  operation: Operation;
  condition: Condition;
}

function getValue(registers: Map<string, number>, key: string) {
  const value = registers.get(key);
  if (value === undefined) throw new Error();
  return value;
}

function evaluateCondition(condition: Condition, registers: Map<string, number>) {
  switch (condition.cond) {
    case Cond.GREATER_THAN:
      return getValue(registers, condition.register) > condition.value;
    case Cond.GREATER_THAN_OR_EQUAL:
      return getValue(registers, condition.register) >= condition.value;
    case Cond.EQUALS:
      return getValue(registers, condition.register) == condition.value;
    case Cond.NOT_EQUALS:
      return getValue(registers, condition.register) != condition.value;
    case Cond.LESS_THAN:
      return getValue(registers, condition.register) < condition.value;
    case Cond.LESS_THAN_OR_EQUALS:
      return getValue(registers, condition.register) <= condition.value;
  }
}

function runOperation(operation: Operation, registers: Map<string, number>) {
  switch (operation.op) {
    case Op.INC:
      return registers.set(operation.register, getValue(registers, operation.register) + operation.value);
    case Op.DEC:
      return registers.set(operation.register, getValue(registers, operation.register) - operation.value);
  }
}

describe("day 08", () => {
  test("part 1 and 2", async () => {
    const input = await readInput("day08-input.txt");
    const instructions = input.map((line) => {
      const parts = line.split(" ");
      return {
        operation: {
          register: parts[0],
          op: parseOp(parts[1]),
          value: parseInt(parts[2]),
        },
        condition: {
          register: parts[4],
          cond: parseCond(parts[5]),
          value: parseInt(parts[6]),
        },
      } as Instruction;
    });
    const registers = new Map<string, number>(instructions.map((ins) => [ins.operation.register, 0]));

    const largest = instructions.map((ins) => {
      if (evaluateCondition(ins.condition, registers)) runOperation(ins.operation, registers);
      return registers.values().toArray().max();
    });

    expect(largest[largest.length - 1]).toBe(6061);
    expect(largest.max()).toBe(6696);
  });
});
