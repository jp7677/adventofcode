#include <iostream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day5 {
   int day5Part1() {
      cout << "Day 5 - Part 1 from https://adventofcode.com/2020/day/5" << endl;

      auto seats = util::loadInputFile("day5-input.txt");

      vector<uint> seatIds;
      transform(seats.begin(), seats.end(), back_inserter(seatIds),
         [](const string seat) {
            auto row = 0U;
            for(int i = 0; i <= 6; i++)
               row = (row << 1) + (seat.at(i) == 'B' ? 1 : 0);

            auto id = 0U;
            for(int i = 6; i <= 9; i++)
               id = (id << 1) + (seat.at(i) == 'R' ? 1 : 0);

            return row * 8 + id;
         });
   
      return seatIds.at(distance(seatIds.begin(), max_element(seatIds.begin(), seatIds.end())));
   }

   TEST_CASE("Day 5 - Part 1") {
      REQUIRE( day5Part1() == 842);
   }

   int day5Part2() {
      cout << "Day 5 - Part 2 from https://adventofcode.com/2020/day/5" << endl;
      return 2;
   }

   TEST_CASE("Day 5 - Part 2") {
      REQUIRE( day5Part2() == 2);
   }
}
