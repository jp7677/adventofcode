#include "days_private.h"

using namespace std;

namespace day15 {
    TEST_CASE("Day 15 - Part 1 from https://adventofcode.com/2020/day/15") {
        auto numbersInputData = util::loadInputFile("day15-input.txt");

        auto numbersInput = util::split(numbersInputData.at(0), ',');

        vector<uint> numbers;
        transform(numbersInput.begin(), numbersInput.end(), back_inserter(numbers),
            [](const auto& number) {
                return stoi(number);
            });

        for (int i = numbers.size(); i < 2020; i++) {
            auto previous = numbers.at(i - 1);

            auto found = find(next(numbers.rbegin()), numbers.rend(), previous);
            if (found == numbers.rend()) {
                numbers.push_back(0);
                continue;
            }

            auto last = find(numbers.rbegin(), numbers.rend(), previous);
            auto lastDistance = numbers.size() - distance(numbers.rbegin(), last);

            auto beforeLast = find(next(last), numbers.rend(), previous);
            auto beforeLastDistance = numbers.size() - distance(numbers.rbegin(), beforeLast);

            numbers.push_back(lastDistance - beforeLastDistance);
        }

        auto result = numbers.back();

        REQUIRE(result == 289);
    }
}
