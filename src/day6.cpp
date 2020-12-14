#include <iostream>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day6 {
   uint day6Part1() {
      cout << "Day 6 - Part 1 from https://adventofcode.com/2020/day/6" << endl;

      auto answers = util::loadInputFile("day6-input.txt");
      
      vector<string> groupedAnswers(1);
      for(auto const& line : answers) {
         if (line != string())
            groupedAnswers.back() += line;
         else
            groupedAnswers.push_back(line);
      }

      return accumulate(groupedAnswers.begin(), groupedAnswers.end(), 0,
         [](auto sum, const auto answersLine) {
            vector<char> answers(0);
            copy(answersLine.begin(), answersLine.end(), back_inserter(answers));
            sort(answers.begin(), answers.end());
            auto last = unique(answers.begin(), answers.end());
            answers.erase(last, answers.end());
            return sum + answers.size();
         });
   }

   TEST_CASE("Day 6 - Part 1") {
      REQUIRE(day6Part1() == 6504);
   }

   uint day6Part2() {
      cout << "Day 6 - Part 2 from https://adventofcode.com/2020/day/6" << endl;
      return 2;
   }

   TEST_CASE("Day 6 - Part 2") {
      REQUIRE(day6Part2() == 2);
   }
}
