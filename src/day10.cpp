#include "days_private.h"

using namespace std;

namespace day10 {
   vector<uint> loadAdapters() {
      auto adaptersData = util::loadInputFile("day10-input.txt");

      vector<uint> adapters;
      transform(adaptersData.begin(), adaptersData.end(), back_inserter(adapters),
         [](const auto& number) {
            return stol(number);
         });

      return adapters;
   }

   TEST_CASE("Day 10 - Part 1 from https://adventofcode.com/2020/day/10") {
      auto adapters = loadAdapters();

      sort(adapters.begin(), adapters.end());

      vector<uint> differences = {adapters.at(0)};
      transform(next(adapters.begin()), adapters.end(), adapters.begin(), back_inserter(differences),
         [](const auto& first, const auto& second) {
            return first - second;
         });

      differences.push_back(3);

      auto diff1 = count_if(differences.begin(), differences.end(),
         [](const auto& diff) {
            return diff == 1;
         });

      auto diff3 = count_if(differences.begin(), differences.end(),
         [](const auto& diff) {
            return diff == 3;
         });

      auto result = diff1 * diff3;

      REQUIRE(result == 1876);
   }
}
