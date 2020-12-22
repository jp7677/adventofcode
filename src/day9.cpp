#include "days_private.h"

using namespace std;

namespace day0 {
   vector<ulong> loadNumbers() {
      auto numbersData = util::loadInputFile("day9-input.txt");

      vector<ulong> numbers;
      transform(numbersData.begin(), numbersData.end(), back_inserter(numbers),
         [](const auto& number) {
            return stol(number);
      });

      return numbers;
   }

   TEST_CASE("Day 9 - Part 1 from https://adventofcode.com/2020/day/9") {
      auto numbers = loadNumbers();
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

   TEST_CASE("Day 9 - Part 2 from https://adventofcode.com/2020/day/9#part2") {
      auto numbers = loadNumbers();
      static const ulong invalid = 1309761972;

      auto position = [numbers]{
         for (auto i = 0U; i < numbers.size(); i++) {
            auto sum = numbers.at(i);

            auto offset = 1U;
            while (sum < invalid) {
               sum += numbers.at(i + offset);
               offset++;
            }

            if (sum == invalid)
               return make_pair(i, i + offset);
         }

         throw ("Invalid data");
      }();

      auto element = minmax_element(numbers.begin() + position.first, numbers.begin() + position.second);
      auto result = *element.first + *element.second;

      REQUIRE(result == 177989832);
   }
}
