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

    template<typename T>
    uint getDestination(const T begin, const T end, const uint current, const uint max) {
        auto destination = current;
        auto it = begin;
        while (it != end) {
            destination = destination == 1 ? max : destination - 1;
            it = find(begin, end, destination);
        }

        return destination;
    }

    void playRoundsWithRotations(vector<uint>& cups, const uint numberOfRounds) {
        auto offset = 0U;
        for (auto round = 0U; round < numberOfRounds; round++) {
            auto currentIndex = (round + offset) % cups.size();
            auto current = cups[currentIndex];

            rotate(cups.begin(), next(cups.begin(), (currentIndex + 4) % cups.size()), cups.end());

            auto destination = getDestination(prev(cups.end(), 3), cups.end(), current, cups.size());
            if (destination != cups[cups.size() - 4])
                rotate(cups.begin(), next(find(cups.begin(), cups.end(), destination)), prev(cups.end(), 3));

            offset = distance(next(cups.begin(), round), find(cups.begin(), cups.end(), current));
        }
    }

    TEST_CASE("Day 23 - Part 1 from https://adventofcode.com/2020/day/23") {
        auto cups = loadCups();

        playRoundsWithRotations(cups, 100);

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

    constexpr uint numberOfCups = 1000000;

    uint nextCup(const array<uint, numberOfCups + 1>& cups, const uint current, const uint steps = 1) {
        auto next = cups[current];
        for (auto i = 1U; i < steps; i++)
            next = cups[next];

        return next;
    }

    void playRoundsWithLinkedElements(array<uint, numberOfCups + 1>& cups, uint current, const uint numberOfRounds) {
        array<uint, 3> pickup{0, 0, 0};
        for (auto round = 0U; round < numberOfRounds; round++) {
            for (auto i = 0U; i < 3; i++)
                pickup[i] = nextCup(cups, current, i + 1);

            auto destination = getDestination(pickup.begin(), pickup.end(), current, numberOfCups);
            cups[current] = nextCup(cups, current, 4);
            cups[pickup[2]] = nextCup(cups, destination);
            cups[destination] = pickup[0];

            current = cups[current];
        }
    }

    TEST_CASE("Day 23 - Part 2 from https://adventofcode.com/2020/day/23#part2") {
        auto initialCups = loadCups();

        array<uint, numberOfCups + 1> cups{0};
        for (auto i = 0U; i < initialCups.size(); i++)
            cups[initialCups[i]] = i + 1 < initialCups.size() ? initialCups[i + 1] : i + 2;

        for (auto i = initialCups.size() + 1; i <= numberOfCups; i++)
            cups[i] = i < numberOfCups ? i + 1 : initialCups[0];

        playRoundsWithLinkedElements(cups, initialCups[0], 10000000);

        auto result = static_cast<ulong>(nextCup(cups, 1)) * nextCup(cups, 1, 2);

        REQUIRE(result == 90481418730);
    }
}
