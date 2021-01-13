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

        vector<uint> lasts(rounds, 0);
        for (auto i = 1U; i <= numbers.size() - 2; i++)
            lasts[numbers.at(i - 1)] = i;

        auto previousLastSpoken = *next(numbers.rbegin());
        auto lastSpoken = numbers.back();
        for (auto i = numbers.size() + 1; i <= rounds; i++) {
            lasts[previousLastSpoken] = i - 2;
            previousLastSpoken = lastSpoken;

            auto last = lasts.at(lastSpoken);
            lastSpoken = last != 0 ? i - last - 1 : 0;
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
