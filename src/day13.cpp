#include "days_private.h"

using namespace std;

namespace day13 {
    TEST_CASE("Day 13 - Part 1 from https://adventofcode.com/2020/day/13") {
        auto notesData = util::loadInputFile("day13-input.txt");

        auto estimate = stoi(notesData.at(0));
        auto busIdData = util::split(notesData.at(1), ',');
        busIdData.erase(remove(busIdData.begin(), busIdData.end(), "x"), busIdData.end());

        vector<pair<uint, uint>> busWaits;
        transform(busIdData.begin(), busIdData.end(), back_inserter(busWaits),
            [&estimate](const auto& data) {
                auto id = stoi(data);
                auto wait = (((estimate / id) * id) + id) - estimate;
                return make_pair(id, wait);
            });

        auto min = min_element(busWaits.begin(), busWaits.end(),
            [](const auto& a, const auto&  b) {
                return a.second < b.second;
            });
        auto result = (*min).first * (*min).second;

        REQUIRE(result == 2545);
    }

    TEST_CASE("Day 13 - Part 2 from https://adventofcode.com/2020/day/13#part2") {
        auto notesData = util::loadInputFile("day13-input.txt");

        auto busIdData = util::split(notesData.at(1), ',');

        vector<pair<uint, uint>> busIds;
        auto offset = 0U;
        for (const auto& id : busIdData) {
            if (id != "x")
                busIds.emplace_back(stoi(id), offset);

            offset++;
        }

        auto first = busIds.at(0);
        vector<pair<uint, uint>> incIds;
        copy_if(busIds.begin(), busIds.end(), back_inserter(incIds),
            [&first](const auto& id) {
                return id.second == first.first || id.second - id.first == first.first;
            });

        busIds.erase(remove_if(busIds.begin(), busIds.end(),
                [&incIds](const auto& id) {
                    return find(incIds.begin(), incIds.end(), id) != incIds.end();
                }),
            busIds.end());

        auto inc = accumulate(incIds.begin(), incIds.end(), static_cast<ulong>(first.first),
            [](const auto product, const auto& id) {
                return product * id.first;
            });

        sort(busIds.begin(), busIds.end(),
            [](const auto& a, const auto& b) {
                return a.first > b.first;
            });

        auto result = inc - first.first;
        while (true) {
            result += inc;
            for (const auto& id : busIds)
                if ((result + id.second) % id.first > 0)
                    goto next;

            break;
            next:;
        }

        REQUIRE(result == 266204454441577);
    }
}
