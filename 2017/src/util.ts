import fs from "fs";
import path from "path";

export const readInput = async (input: string) =>
  (await fs.promises.readFile(path.join(__dirname, `../data/${input}`)))
    .toString()
    .trimEnd()
    .split("\n");

declare global {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface Array<T> {
    max(): number;
    min(): number;
  }
}

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
