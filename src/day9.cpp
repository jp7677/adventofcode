#include "days_private.h"

using namespace std;

namespace day0 {
   TEST_CASE("Day 9 - Part 1 from https://adventofcode.com/2020/day/9") {
      auto numbersData = util::loadInputFile("day9-input.txt");

      vector<ulong> numbers;
      transform(numbersData.begin(), numbersData.end(), back_inserter(numbers),
         [](const auto& number) {
            return stol(number);
      });
      
      static const uint preamble = 25;
      auto result = [numbers]{
         for (auto i = preamble; i <= numbers.size(); i++) {
            set<ulong> sums;
            for (auto k = i - preamble ; k < i; k++)
               for (auto l = i - preamble; l < i; l++)
                  if (numbers.at(k) != numbers.at(l))
                     sums.insert(numbers.at(k) + numbers.at(l));

            if (sums.find(numbers.at(i)) == sums.end())
               return numbers.at(i);
         }

         throw ("Invalid data");
      }();

      REQUIRE(result == 1309761972);
   }
}
