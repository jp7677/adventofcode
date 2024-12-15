import fs from "fs";
import path from "path";

export const readInput = async (input: string) =>
    (await fs.promises.readFile(path.join(__dirname, `../data/${input}`)))
        .toString()
        .trimEnd()
        .split('\n');
