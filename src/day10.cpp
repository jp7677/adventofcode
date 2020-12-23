#include "days_private.h"

using namespace std;

namespace day10 {
   vector<uint> loadAdapters() {
      auto adaptersData = util::loadInputFile("day10-input.txt");

      vector<uint> adapters;
      transform(adaptersData.begin(), adaptersData.end(), back_inserter(adapters),
         [](const auto& number) {
            return stoi(number);
         });

      return adapters;
   }

   TEST_CASE("Day 10 - Part 1 from https://adventofcode.com/2020/day/10") {
      auto adapters = loadAdapters();

      sort(adapters.begin(), adapters.end());
      adjacent_difference(adapters.begin(), adapters.end(), adapters.begin());
      adapters.push_back(3);

      auto diff1 = count(adapters.begin(), adapters.end(), 1);
      auto diff3 = count(adapters.begin(), adapters.end(), 3);
      auto result = diff1 * diff3;

      REQUIRE(result == 1876);
   }
}
