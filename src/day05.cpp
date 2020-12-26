#include "days_private.h"

using namespace std;

namespace day05 {
   vector<uint> loadSeatIds() {
      auto seatsInput = util::loadInputFile("day05-input.txt");

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

   TEST_CASE("Day 5 - Part 1 from https://adventofcode.com/2020/day/5") {
      auto seatIds = loadSeatIds();

      auto result = seatIds.at(distance(seatIds.begin(), max_element(seatIds.begin(), seatIds.end())));

      REQUIRE(result == 842);
   }

   TEST_CASE("Day 5 - Part 2 from https://adventofcode.com/2020/day/5#part2") {
      auto seatIds = loadSeatIds();

      sort(seatIds.begin(), seatIds.end());

      auto result = [&seatIds]{
         for (auto i = 0U; i <= seatIds.size(); i ++)
            if (seatIds.at(i) != seatIds.at(i + 1) - 1)
               return seatIds.at(i) + 1;

         throw ("Invalid data found");
      }();

      REQUIRE(result == 617);
   }
}
