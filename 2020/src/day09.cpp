#include "days_private.h"

using namespace std;

namespace day09 {
    vector<uint> loadNumbers() {
        auto numbersData = util::loadInputFile("day09-input.txt");

        vector<uint> numbers;
        transform(numbersData.begin(), numbersData.end(), back_inserter(numbers),
            [](const auto& number) {
                return stol(number);
            });

        return numbers;
    }

    TEST_CASE("Day 09 - Part 1 from https://adventofcode.com/2020/day/9") {
        auto numbers = loadNumbers();
        static constexpr auto preamble = 25U;

        auto result = [&numbers] {
            for (auto i = preamble; i < numbers.size(); i++) {
                auto found = [&numbers, i] {
                    for (auto k = i - preamble; k < i; k++)
                        for (auto l = i - preamble; l < i; l++)
                            if (numbers[k] != numbers[l] && numbers[k] + numbers[l] == numbers[i])
                                return true;

                    return false;
                }();

                if (!found)
                    return numbers[i];
            }

            throw runtime_error("Invalid data");
        }();

        REQUIRE(result == 1309761972);
    }

    TEST_CASE("Day 09 - Part 2 from https://adventofcode.com/2020/day/9#part2") {
        auto numbers = loadNumbers();
        static constexpr auto invalid = 1309761972U;

        auto position = [&numbers] {
            for (auto i = 0U; i < numbers.size(); i++) {
                for (auto sum = numbers[i], offset = 1U; sum < invalid; offset++) {
                    sum += numbers[i + offset];
                    if (sum == invalid)
                        return make_pair(i, i + ++offset);
                }
            }

            throw runtime_error("Invalid data");
        }();

        auto element = minmax_element(next(numbers.begin(), position.first), next(numbers.begin(), position.second));
        auto result = *element.first + *element.second;

        REQUIRE(result == 177989832);
    }
}
