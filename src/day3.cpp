#include <iostream>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

ulong runSlope(vector<string>* grid, int right, int down) {
   auto width = grid->at(0).size();
   auto height = grid->size();

   auto trees = 0U;
   auto x = 0U;
   auto y = 0U;

   while (y < height) {
      if (x >= width)
         x -= width;

      if (grid->at(y).at(x) == '#')
         trees++;

      y += down;
      x += right;
   }
   
   return trees;
}

ulong day3Part1() {
   cout << "Day 3 - Part 1 from https://adventofcode.com/2020/day/3" << endl;

   auto grid = loadInputFile("day3-input.txt");
   return runSlope(&grid, 3, 1);
}

TEST_CASE("Day 3 - Part 1") {
   REQUIRE( day3Part1() == 265);
}

ulong day3Part2() {
   cout << "Day 3 - Part 2 from https://adventofcode.com/2020/day/3" << endl;

   auto grid = loadInputFile("day3-input.txt");
   return runSlope(&grid, 1, 1) *
      runSlope(&grid, 3, 1) *
      runSlope(&grid, 5, 1) *
      runSlope(&grid, 7, 1) *
      runSlope(&grid, 1, 2);
}

TEST_CASE("Day 3 - Part 2") {
   REQUIRE( day3Part2() == 3154761400);
}
