#include "days_private.h"

using namespace std;

namespace day23 {
    TEST_CASE("Day 23 - Part 1 from https://adventofcode.com/2020/day/23") {
        auto cup = util::loadInputFile("day23-input.txt");

        vector<uint> values;
        transform(cup[0].begin(), cup[0].end(), back_inserter(values),
            [](const auto& value) {
                return value - '0';
            });

        for (auto round = 0U; round < 100; round ++) {
            auto current = round % values.size();
            auto currentValue = values[current];

            vector<uint> pickup{values[(current + 1) % values.size()], values[(current + 2) % values.size()], values[(current + 3) % values.size()]};
            for (const auto v : pickup)
                values.erase(find(values.begin(), values.end(), v), next(find(values.begin(), values.end(), v)));

            auto destination = currentValue;
            auto it = pickup.begin();
            while (it != pickup.end()) {
                destination = destination - 1 < *min_element(values.begin(), values.end())
                    ? *max_element(values.begin(), values.end())
                    : destination - 1;
                it = find(pickup.begin(), pickup.end(), destination);
            }

            it = find(values.begin(), values.end(), destination);
            for (auto i = pickup.size(); i > 0; i--)
                values.insert(next(it), pickup[i - 1]);

            while (currentValue != values[current]) {
                values.push_back(values.front());
                values.erase(values.begin(),next(values.begin()));
            }
        }

        string result;
        auto it = find(values.begin(), values.end(), 1);
        transform(next(it), values.end(), back_inserter(result),
            [](const auto& value){
                return to_string(value)[0];
            });
        transform(values.begin(), it, back_inserter(result),
            [](const auto& value){
                return to_string(value)[0];
            });

        REQUIRE(result == "36472598");
    }
}
