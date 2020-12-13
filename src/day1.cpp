#include <iostream>
#include <vector>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

int day1Part1() {
   cout << "Day 1 - Part 1 from https://adventofcode.com/2020/day/1" << endl;

   auto report = util::loadInputFile("day1-input.txt");
   for (auto const& element1 : report)
      for (auto const& element2 : report) {
         auto expense1 = stoi(element1);
         auto expense2 = stoi(element2);
         if (expense1 + expense2 == 2020)
            return expense1 * expense2;
      }

   throw("invalid data");
}

TEST_CASE("Day 1 - Part 1") {
   REQUIRE(day1Part1() == 964875);
}

int day1Part2() {
   cout << "Day 1 - Part 2 from https://adventofcode.com/2020/day/1" << endl;

   auto report = util::loadInputFile("day1-input.txt");
   for (auto const& element1 : report)
      for (auto const& element2 : report)
         for (auto const& element3 : report) {
            auto expense1 = stoi(element1);
            auto expense2 = stoi(element2);
            auto expense3 = stoi(element3);
            if (expense1 + expense2 + expense3 == 2020)
               return expense1 * expense2 * expense3;
         }

   throw("invalid data");
}

TEST_CASE("Day 1 - Part 2") {
   REQUIRE(day1Part2() == 158661360);
}
