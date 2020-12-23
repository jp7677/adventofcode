#include "days_private.h"

using namespace std;

namespace day10 {
   vector<uint> loadAdapterDifferences() {
      auto adaptersData = util::loadInputFile("day10-input.txt");

      vector<uint> adapters;
      transform(adaptersData.begin(), adaptersData.end(), back_inserter(adapters),
         [](const auto& number) {
            return stoi(number);
         });

      sort(adapters.begin(), adapters.end());
      adjacent_difference(adapters.begin(), adapters.end(), adapters.begin());
      adapters.push_back(3);

      return adapters;
   }

   TEST_CASE("Day 10 - Part 1 from https://adventofcode.com/2020/day/10") {
      auto differences = loadAdapterDifferences();

      auto diff1 = count(differences.begin(), differences.end(), 1);
      auto diff3 = count(differences.begin(), differences.end(), 3);
      auto result = diff1 * diff3;

      REQUIRE(result == 1876);
   }


   TEST_CASE("Day 10 - Part 2 from https://adventofcode.com/2020/day/10#part2") {
      auto differences = loadAdapterDifferences();

      vector<uint> diff1groups {1};
      for (auto i = 1U; i < differences.size(); i++) {
         if (differences.at(i) == 1 && differences.at(i - 1) == 1)
            diff1groups.back()++;
         else
            diff1groups.push_back(1);
      }

      auto result = accumulate(diff1groups.begin(), diff1groups.end(), 1UL,
         [](const auto product, const auto& group) {
            return product * (group == 4 ? 7 : group == 3 ? 4 : group == 2 ? 2 : 1);
         });

      REQUIRE(result == 14173478093824);
   }
}
