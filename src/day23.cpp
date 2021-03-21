#include "days_private.h"

using namespace std;

namespace day23 {
    vector<uint> loadCups() {
        auto cupLabeling = util::loadInputFile("day23-input.txt");

        vector<uint> cups;
        transform(cupLabeling[0].begin(), cupLabeling[0].end(), back_inserter(cups),
            [](const auto& label) {
                return label - '0';
            });

        return cups;
    }

    void playRounds(vector<uint>& cups, uint numberOfRounds) {
        auto offset = 0U;
        for (auto round = 0U; round < numberOfRounds; round++) {
            auto currentIndex = (round + offset) % cups.size();
            auto current = cups[currentIndex];

            rotate(cups.begin(), cups.begin() + (currentIndex + 4) % cups.size(), cups.end());

            auto destination = current;
            auto it = cups.begin();
            while (it != cups.end()) {
                destination = destination == 1 ? cups.size() : destination - 1;
                it = find(cups.end() - 3, cups.end(), destination);
            }

            if (destination != cups[cups.size() - 4])
                rotate(cups.begin(), find(cups.begin(), cups.end(), destination) + 1, cups.end() - 3);

            offset = distance(cups.begin() + round, find(cups.begin(), cups.end(), current));
        }
    }

    TEST_CASE("Day 23 - Part 1 from https://adventofcode.com/2020/day/23") {
        auto cups = loadCups();

        playRounds(cups, 100);

        string result;
        auto it = find(cups.begin(), cups.end(), 1);
        transform(next(it), cups.end(), back_inserter(result),
            [](const auto& label){
                return to_string(label)[0];
            });
        transform(cups.begin(), it, back_inserter(result),
            [](const auto& label){
                return to_string(label)[0];
            });

        REQUIRE(result == "36472598");
    }

    TEST_CASE("Day 23 - Part 2 from https://adventofcode.com/2020/day/23#part2","[.skip-due-to-horrible-performance]") {
        auto initialCups = loadCups();

        auto cups = vector<uint>(initialCups);
        for (auto i = initialCups.size() + 1; i <= 1000000; i++)
            cups.push_back(i);

        playRounds(cups, 10000000);

        auto it = find(cups.begin(), cups.end(), 1);
        auto result = static_cast<ulong>(*next(it) * *next(it, 2));

        REQUIRE(result == 90481418730);
    }
}
