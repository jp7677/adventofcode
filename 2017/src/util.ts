import fs from "fs";
import path from "path";

export const readInput = async (input: string) =>
  (await fs.promises.readFile(path.join(__dirname, `../data/${input}`))).toString().trimEnd().split("\n");

export const repeat = (rounds: number, fn: (i: number) => void) => {
  for (let r = 0; r < rounds; r++) fn(r);
};

declare global {
  interface Array<T> {
    toSet(): Set<T>;
    max(): number;
    min(): number;
    sum(): number;
  }
}

Array.prototype.toSet = function () {
  return new Set(this);
};

Array.prototype.max = function () {
  return this.reduce((acc, it) => {
    if (it > acc) return it;
    else return acc;
  }, 0);
};

Array.prototype.min = function () {
  return this.reduce((acc, it) => {
    if (it < acc) return it;
    else return acc;
  });
};

Array.prototype.sum = function () {
  return this.reduce((acc, it) => acc + it);
};
