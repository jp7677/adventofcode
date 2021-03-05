#include "days_private.h"

using namespace std;

namespace day03 {
    ulong runSlope(const vector<string>& grid, const uint right, const uint down) {
        auto width = grid[0].size();
        auto height = grid.size();

        auto trees = 0U;
        auto x = 0U;
        auto y = 0U;

        while (y < height) {
            if (grid[y][x % width] == '#')
                trees++;

            y += down;
            x += right;
        }

        return trees;
    }

    TEST_CASE("Day 03 - Part 1 from https://adventofcode.com/2020/day/3") {
        auto gridInput = util::loadInputFile("day03-input.txt");

        auto result = runSlope(gridInput, 3, 1);

        REQUIRE(result == 265);
    }

    TEST_CASE("Day 03 - Part 2 from https://adventofcode.com/2020/day/3#part2") {
        auto gridInput = util::loadInputFile("day03-input.txt");

        auto result = runSlope(&gridInput, 1, 1) *
            runSlope(&gridInput, 3, 1) *
            runSlope(&gridInput, 5, 1) *
            runSlope(&gridInput, 7, 1) *
            runSlope(&gridInput, 1, 2);

        REQUIRE(result == 3154761400);
    }
}
