#include <iostream>
#include <regex>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day2 {
   uint day2Part1() {
      cout << "Day 2 - Part 1 from https://adventofcode.com/2020/day/2" << endl;

      auto passwordEntriesInput = util::loadInputFile("day2-input.txt");

      auto hits = 0U;
      for (const auto& passwordEntry : passwordEntriesInput) {
         smatch matches;
         auto success = regex_search(passwordEntry, matches, regex("(\\d+)\\-(\\d+)\\ (\\w)\\:\\s(\\w+)"));
         if(!success)
            throw("Invalid data found");

         auto minOccurences = stoi(matches[1].str());
         auto maxOccurences = stoi(matches[2].str());
         auto character = matches[3].str().at(0);
         auto password = matches[4].str();

         auto count = 0U;
         for (const auto& passwordCharacter : password)
            if (passwordCharacter == character)
               count++;

         if (count >= minOccurences && count <= maxOccurences)
            hits++;
      }

      return hits;
   }

   TEST_CASE("Day 2 - Part 1") {
      REQUIRE(day2Part1() == 422);
   }

   uint day2Part2() {
      cout << "Day 2 - Part 2 from https://adventofcode.com/2020/day/2" << endl;

      auto passwordEntriesInput = util::loadInputFile("day2-input.txt");

      auto hits = 0U;
      for (const auto& passwordEntry : passwordEntriesInput) {
         smatch matches;
         auto success = regex_search(passwordEntry, matches, regex("(\\d+)\\-(\\d+)\\ (\\w)\\:\\s(\\w+)"));
         if(!success)
            throw("Invalid data found");

         auto first = stoi(matches[1].str());
         auto second = stoi(matches[2].str());
         auto character = matches[3].str().at(0);
         auto password = matches[4].str();

         auto match = 0U;
         if (password.at(first - 1) == character)
            match++; 

         if (password.at(second - 1) == character)
            match++;

         if (match == 1)
            hits++;
      }

      return hits;
   }

   TEST_CASE("Day 2 - Part 2") {
      REQUIRE(day2Part2() == 451);
   }
}
