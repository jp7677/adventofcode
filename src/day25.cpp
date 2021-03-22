#include "days_private.h"

using namespace std;

namespace day25 {
    TEST_CASE("Day 25 - Part 1 from https://adventofcode.com/2020/day/25") {
        auto keys = util::loadInputFile("day25-input.txt");

        auto cardKey = stoul(keys[0]);
        auto doorKey = stoul(keys[1]);

        auto cardLoopSize = 0U;
        auto value = 1U;
        while (value != cardKey) {
            value = (value * 7) % 20201227;
            cardLoopSize++;
        }

        auto result = 1U;
        for (auto i = 0U; i < cardLoopSize; i++)
            result = (result * doorKey) % 20201227;

        REQUIRE(result == 8740494);
    }
}
