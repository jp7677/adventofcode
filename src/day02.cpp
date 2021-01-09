#include "days_private.h"

using namespace std;

namespace day02 {
   TEST_CASE("Day 02 - Part 1 from https://adventofcode.com/2020/day/2") {
      auto passwordEntriesInput = util::loadInputFile("day02-input.txt");

      auto re = regex(R"((\d+)\-(\d+)\ (\w)\:\s(\w+))");
      auto result = count_if(passwordEntriesInput.begin(), passwordEntriesInput.end(),
         [&re](const auto& passwordEntry) {
            smatch matches;
            auto success = regex_search(passwordEntry, matches, re);
            if (!success)
               throw runtime_error("Invalid data found");

            auto minOccurrences = stoul(matches[1].str());
            auto maxOccurrences = stoul(matches[2].str());
            auto character = matches[3].str().at(0);
            auto password = matches[4].str();

            auto count = count_if(password.begin(), password.end(),
               [&character](const auto& passwordCharacter) {
                  return passwordCharacter == character;
               });

            return count >= minOccurrences && count <= maxOccurrences;
         });

      REQUIRE(result == 422);
   }

   TEST_CASE("Day 02 - Part 2 from https://adventofcode.com/2020/day/2#part2") {
      auto passwordEntriesInput = util::loadInputFile("day02-input.txt");

      auto re = regex(R"((\d+)\-(\d+)\ (\w)\:\s(\w+))");
      auto result = count_if(passwordEntriesInput.begin(), passwordEntriesInput.end(),
         [&re](const auto& passwordEntry) {
            smatch matches;
            auto success = regex_search(passwordEntry, matches, re);
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

            return match == 1;
         });

      REQUIRE(result == 451);
   }
}
