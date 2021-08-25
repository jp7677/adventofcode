#include "days_private.h"

using namespace std;

namespace day01 {
    vector<int> loadReportInput() {
        auto reportInputData = util::loadInputFile("day01-input.txt");

        vector<int> reportInput;
        transform(reportInputData.begin(), reportInputData.end(), back_inserter(reportInput),
            [](const auto& element) {
                return stoi(element);
            });

        return reportInput;
    }

    TEST_CASE("Day 01 - Part 1 from https://adventofcode.com/2020/day/1") {
        auto reportInput = loadReportInput();

        auto result = [&reportInput] {
            for (const auto& expense1 : reportInput)
                for (const auto& expense2 : reportInput)
                    if (expense1 + expense2 == 2020)
                        return expense1 * expense2;

            throw runtime_error("invalid data");
        }();

        REQUIRE(result == 964875);
    }

    TEST_CASE("Day 01 - Part 2 from https://adventofcode.com/2020/day/1#part2") {
        auto reportInput = loadReportInput();

        auto result = [&reportInput] {
            for (const auto& expense1 : reportInput)
                for (const auto& expense2 : reportInput)
                    for (const auto& expense3 : reportInput)
                        if (expense1 + expense2 + expense3 == 2020)
                            return expense1 * expense2 * expense3;

            throw runtime_error("invalid data");
        }();

        REQUIRE(result == 158661360);
    }
}
