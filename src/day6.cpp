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

      auto answers = util::loadInputFile("day6-input.txt");
      
      vector<vector<string>> groupedAnswers(1);
      for(auto const& line : answers) {
         if (line != string())
            groupedAnswers.back().push_back(line);
         else
            groupedAnswers.push_back(vector<string>());
      }

      return accumulate(groupedAnswers.begin(), groupedAnswers.end(), 0,
         [](auto sum, const auto answersList) {
            if (answersList.size() == 1)
               sum + answersList.at(0).size();

            auto current = answersList.at(0);
            sort(current.begin(), current.end());
            for(auto i = 1U; i < answersList.size(); i++) {
               auto next = answersList.at(i);
               sort(next.begin(), next.end());

               string intersection;
               set_intersection(current.begin(), current.end(), next.begin(), next.end(), back_inserter(intersection));

               current = intersection;
            }
            return sum + current.size();
         });
   }

   TEST_CASE("Day 6 - Part 2") {
      REQUIRE(day6Part2() == 3351);
   }
}
