#include "days_private.h"

using namespace std;

namespace day1 {
   TEST_CASE("Day 1 - Part 1 from https://adventofcode.com/2020/day/1") {
      auto reportInput = util::loadInputFile("day1-input.txt");

      auto result = [reportInput]{
         for (const auto& element1 : reportInput)
            for (const auto& element2 : reportInput) {
               auto expense1 = stoi(element1);
               auto expense2 = stoi(element2);
               if (expense1 + expense2 == 2020)
                  return expense1 * expense2;
            }

         throw ("invalid data");
      }();

      REQUIRE(result == 964875);
   }

   TEST_CASE("Day 1 - Part 2 from https://adventofcode.com/2020/day/1#part2") {
      auto reportInput = util::loadInputFile("day1-input.txt");

      auto result = [reportInput]{
         for (const auto& element1 : reportInput)
            for (const auto& element2 : reportInput)
               for (const auto& element3 : reportInput) {
                  auto expense1 = stoi(element1);
                  auto expense2 = stoi(element2);
                  auto expense3 = stoi(element3);
                  if (expense1 + expense2 + expense3 == 2020)
                     return expense1 * expense2 * expense3;
               }

         throw ("invalid data");
      }();

      REQUIRE(result == 158661360);
   }
}