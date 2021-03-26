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

    void playRoundsWithRotations(vector<uint>& cups, uint numberOfRounds) {
        auto offset = 0U;
        for (auto round = 0U; round < numberOfRounds; round++) {
            auto currentIndex = (round + offset) % cups.size();
            auto current = cups[currentIndex];

            rotate(cups.begin(), next(cups.begin(), (currentIndex + 4) % cups.size()), cups.end());

            auto destination = current;
            auto it = cups.begin();
            while (it != cups.end()) {
                destination = destination == 1 ? cups.size() : destination - 1;
                it = find(prev(cups.end(), 3), cups.end(), destination);
            }

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

    uint nextCup(unordered_map<uint, pair<uint, uint>>& cups, uint current, uint steps = 1) {
        auto it = cups.find(current);
        for (auto i = 1U; i < steps; i++)
            it = cups.find(it->second.second);

        return it->second.second;
    }

    void playRoundsWithLinkedElements(unordered_map<uint, pair<uint, uint>>& cups, uint first, uint numberOfRounds) {
        auto current = first;
        array<uint, 3> pickup{0, 0, 0};
        for (auto round = 0U; round < numberOfRounds; round++) {
            for (auto i = 0U; i < 3; i++)
                pickup[i] = nextCup(cups, current, i + 1);

            auto destination = current;
            auto it = pickup.begin();
            while (it != pickup.end()) {
                destination = destination == 1 ? cups.size() : destination - 1;
                it = find(pickup.begin(), pickup.end(), destination);
            }

            cups[current].second = nextCup(cups, current, 4);
            cups[nextCup(cups, current)].first = current;
            cups[pickup[2]].second = nextCup(cups, destination);
            cups[nextCup(cups, destination)].first = pickup[2];
            cups[destination].second = pickup[0];
            cups[pickup[0]].first = destination;

            current = cups[current].second;
        }
    }

    TEST_CASE("Day 23 - Part 2 from https://adventofcode.com/2020/day/23#part2") {
        auto initialCups = loadCups();

        constexpr uint numberOfCups = 1000000;
        unordered_map<uint, pair<uint, uint>> cups;
        for (auto i = 0U; i < initialCups.size(); i++)
            cups.insert({
                initialCups[i], {
                    i > 0 ? initialCups[i - 1] : numberOfCups,
                    i + 1 < initialCups.size() ? initialCups[i + 1] : i + 2}});

        for (auto i = initialCups.size() + 1; i <= numberOfCups; i++)
            cups.insert({
                i, {
                    i - 1 > initialCups.size() ? i - 1 : initialCups[initialCups.size() - 1],
                    i < numberOfCups ? i + 1 : initialCups[0]}});

        playRoundsWithLinkedElements(cups, initialCups[0], 10000000);

        auto result = static_cast<ulong>(nextCup(cups, 1)) * nextCup(cups, 1, 2);

        REQUIRE(result == 90481418730);
    }
}
