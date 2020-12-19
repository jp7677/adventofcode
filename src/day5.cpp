#include <iostream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day5 {
   vector<uint> loadSeatIds() {
      auto seatsInput = util::loadInputFile("day5-input.txt");

      vector<uint> seatIds;
      transform(seatsInput.begin(), seatsInput.end(), back_inserter(seatIds),
         [](const auto& seat) {
            auto row = 0U;
            for (auto i = 0U; i <= 6; i++)
               row = (row << 1) + (seat.at(i) == 'B' ? 1 : 0);

            auto id = 0U;
            for (auto i = 6U; i <= 9; i++)
               id = (id << 1) + (seat.at(i) == 'R' ? 1 : 0);

            return row * 8 + id;
         });

      return seatIds;
   }

   uint day5Part1() {
      cout << "Day 5 - Part 1 from https://adventofcode.com/2020/day/5" << endl;

      auto seatIds = loadSeatIds();
      return seatIds.at(distance(seatIds.begin(), max_element(seatIds.begin(), seatIds.end())));
   }

   TEST_CASE("Day 5 - Part 1") {
      REQUIRE(day5Part1() == 842);
   }

   uint day5Part2() {
      cout << "Day 5 - Part 2 from https://adventofcode.com/2020/day/5" << endl;

      auto seatIds = loadSeatIds();
      sort(seatIds.begin(), seatIds.end());

      for (auto i = 0U; i <= seatIds.size(); i ++)
         if (seatIds.at(i) != seatIds.at(i + 1) - 1)
            return seatIds.at(i) + 1;

      throw ("Invalid data found");
   }

   TEST_CASE("Day 5 - Part 2") {
      REQUIRE(day5Part2() == 617);
   }
}
