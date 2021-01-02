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

   TEST_CASE("Day 13 - Part 2 from https://adventofcode.com/2020/day/13#part2", "[.]" /* Disabled */ ) {
      auto notesData = util::loadInputFile("day13-input.txt");

      auto busIdData = util::split(notesData.at(1), ',');

      vector<pair<uint, uint>> busIds;
      auto offset = 0U;
      for (const auto& id : busIdData) {
         if (id != "x")
            busIds.push_back(make_pair(stoi(id), offset));

         offset++;
      }

      auto product = accumulate(busIds.begin(), busIds.end(), 1UL,
         [](const auto product, const auto& id) {
            return product * id.first;
         });

      auto first = busIds.at(0);
      auto it = find_if(busIds.begin(), busIds.end(),
         [&first](const auto& id) {
            return id.second == first.first;
         });
      auto inc = first.first * (it != busIds.end() ? (*it).first : 1);
      auto result = product - first.first;
      while (result > inc) {
         result -= inc;
         for (auto i = 1; i < busIds.size(); i++)
            if ((result + busIds.at(i).second) % busIds.at(i).first > 0)
               goto next;

         break;
         next:;
      }

      REQUIRE(result == 266204454441577);
   }
}
