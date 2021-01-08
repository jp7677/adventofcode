#include "days_private.h"

using namespace std;

namespace day06 {
   TEST_CASE("Day 06 - Part 1 from https://adventofcode.com/2020/day/6") {
      auto answersListInput = util::loadInputFile("day06-input.txt");

      vector<string> answersList(1);
      for (const auto& line : answersListInput) {
         if (!line.empty())
            answersList.back() += line;
         else
            answersList.push_back(line);
      }

      auto result = accumulate(answersList.begin(), answersList.end(), 0U,
         [](const auto sum, auto& answers) { // We cannot use `const auto& answers`/immmutable because of `unique`
            sort(answers.begin(), answers.end());
            return sum + distance(answers.begin(), unique(answers.begin(), answers.end()));
         });

      REQUIRE(result == 6504);
   }

   TEST_CASE("Day 06 - Part 2 from https://adventofcode.com/2020/day/6#part2") {
      auto answersListInput = util::loadInputFile("day06-input.txt");

      vector<vector<string>> answersList(1);
      for (const auto& line : answersListInput) {
         if (!line.empty())
            answersList.back().push_back(line);
         else
            answersList.push_back(vector<string>());
      }

      auto result = accumulate(answersList.begin(), answersList.end(), 0U,
         [](const auto sum, const auto& answers) {
            if (answers.size() == 1)
               sum + answers.at(0).size();

            auto intersected = answers.at(0);
            sort(intersected.begin(), intersected.end());
            for (auto i = 1U; i < answers.size(); i++) {
               auto next = answers.at(i);
               sort(next.begin(), next.end());

               string intersection;
               set_intersection(intersected.begin(), intersected.end(), next.begin(), next.end(), back_inserter(intersection));
               intersected = intersection;
            }
            return sum + intersected.size();
         });

      REQUIRE(result == 3351);
   }
}
