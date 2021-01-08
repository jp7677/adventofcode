#include "days_private.h"

using namespace std;

namespace day02 {
   TEST_CASE("Day 02 - Part 1 from https://adventofcode.com/2020/day/2") {
      auto passwordEntriesInput = util::loadInputFile("day02-input.txt");

      auto result = 0U;
      for (const auto& passwordEntry : passwordEntriesInput) {
         smatch matches;
         auto success = regex_search(passwordEntry, matches, regex(R"((\d+)\-(\d+)\ (\w)\:\s(\w+))"));
         if (!success)
            throw runtime_error("Invalid data found");

         auto minOccurrences = stoul(matches[1].str());
         auto maxOccurrences = stoul(matches[2].str());
         auto character = matches[3].str().at(0);
         auto password = matches[4].str();

         auto count = 0U;
         for (const auto& passwordCharacter : password)
            if (passwordCharacter == character)
               count++;

         if (count >= minOccurrences && count <= maxOccurrences)
            result++;
      }

      REQUIRE(result == 422);
   }

   TEST_CASE("Day 02 - Part 2 from https://adventofcode.com/2020/day/2#part2") {
      auto passwordEntriesInput = util::loadInputFile("day02-input.txt");

      auto result = 0U;
      for (const auto& passwordEntry : passwordEntriesInput) {
         smatch matches;
         auto success = regex_search(passwordEntry, matches, regex(R"((\d+)\-(\d+)\ (\w)\:\s(\w+))"));
         if (!success)
            throw runtime_error("Invalid data found");

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
            result++;
      }

      REQUIRE(result == 451);
   }
}
