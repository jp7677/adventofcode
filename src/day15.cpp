#include "days_private.h"

using namespace std;

namespace day15 {
    uint playRounds(uint rounds) {
        auto numbersInput = util::split(util::loadInputFile("day15-input.txt").at(0), ',');

        vector<uint> numbers;
        transform(numbersInput.begin(), numbersInput.end(), back_inserter(numbers),
              [](const auto& number) {
                  return stoi(number);
              });

        unordered_map<uint, uint> lasts(rounds / 10);
        for (auto i = 0U; i < numbers.size() - 2; i++)
            lasts.insert_or_assign(numbers.at(i), i);

        auto previousLastSpoken = *next(numbers.rbegin());
        auto lastSpoken = numbers.back();
        for (auto i = numbers.size(); i < rounds; i++) {
            lasts.insert_or_assign(previousLastSpoken, i - 2);
            previousLastSpoken = lastSpoken;

            auto last = lasts.find(lastSpoken);
            if (last == lasts.end())
                lastSpoken = 0;
            else
                lastSpoken = i - last->second - 1;
        }

        return lastSpoken;
    }

    TEST_CASE("Day 15 - Part 1 from https://adventofcode.com/2020/day/15") {
        auto result = playRounds(2020);

        REQUIRE(result == 289);
    }

    TEST_CASE("Day 15 - Part 2 from https://adventofcode.com/2020/day/15#part2") {
        auto result = playRounds(30000000);

        REQUIRE(result == 1505722);
    }
}
