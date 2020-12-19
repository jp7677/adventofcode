#include <iostream>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day6 {
   uint day6Part1() {
      cout << "Day 6 - Part 1 from https://adventofcode.com/2020/day/6" << endl;

      auto answersListInput = util::loadInputFile("day6-input.txt");
      
      vector<string> answersList(1);
      for (const auto& line : answersListInput) {
         if (line != string())
            answersList.back() += line;
         else
            answersList.push_back(line);
      }

      return accumulate(answersList.begin(), answersList.end(), 0U,
         [](const auto sum, auto& answers) { // We cannot use `const auto& answers`/immmutable because of `unique`
            sort(answers.begin(), answers.end());
            return sum + distance(answers.begin(), unique(answers.begin(), answers.end()));
         });
   }

   TEST_CASE("Day 6 - Part 1") {
      REQUIRE(day6Part1() == 6504);
   }

   uint day6Part2() {
      cout << "Day 6 - Part 2 from https://adventofcode.com/2020/day/6#part2" << endl;

      auto answersListInput = util::loadInputFile("day6-input.txt");
      
      vector<vector<string>> answersList(1);
      for (const auto& line : answersListInput) {
         if (line != string())
            answersList.back().push_back(line);
         else
            answersList.push_back(vector<string>());
      }

      return accumulate(answersList.begin(), answersList.end(), 0U,
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
   }

   TEST_CASE("Day 6 - Part 2") {
      REQUIRE(day6Part2() == 3351);
   }
}
