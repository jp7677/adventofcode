#include "days_private.h"

using namespace std;

namespace day00 {
    TEST_CASE("Day 18 - Part 1 from https://adventofcode.com/2020/day/18") {
        auto mathData = util::loadInputFile("day18-input.txt");

        auto result = accumulate(mathData.begin(), mathData.end(), 0UL,
            [](const auto sum, const auto& mathLine) {
                stack<pair<ulong, char>> results;
                results.emplace(0UL, '+');
                for (const auto token : mathLine) {
                    auto number = 0UL;
                    switch (token) {
                        case ' ': continue;
                        case '+':
                        case '*':
                            results.top().second = token;
                            continue;
                        case '(':
                            results.emplace(0UL, '+');
                            continue;
                        case ')':
                            number = results.top().first;
                            results.pop();
                            break;
                        default :
                            number = stoi(string(1, token));
                    }

                    results.top().first = results.top().second == '+'
                        ? results.top().first + number
                        : results.top().first * number;
                }
                return sum + results.top().first;
            });

        REQUIRE(result == 4297397455886);
    }
}
