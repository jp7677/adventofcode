#include "days_private.h"

using namespace std;

namespace day13 {
   TEST_CASE("Day 13 - Part 1 from https://adventofcode.com/2020/day/13") {
      auto notesData = util::loadInputFile("day13-input.txt");

      auto estimate = stoi(notesData.at(0));
      auto busIdData = util::split(notesData.at(1), ',');
      busIdData.erase(remove(busIdData.begin(), busIdData.end(), "x"), busIdData.end());

      vector<pair<uint, uint>> busWaits;
      transform(busIdData.begin(), busIdData.end(), back_inserter(busWaits),
         [estimate](const auto& data) {
            auto id = stoi(data);
            auto wait = (((estimate / id) * id) + id) - estimate;
            return make_pair(id, wait);
         });

      auto min = min_element(busWaits.begin(), busWaits.end(),
         [](const auto& a, const auto&  b) {
            return a.second < b.second;
         });
      auto result = (*min).first * (*min).second;

      REQUIRE(result == 2545);
   }
}
