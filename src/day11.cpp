#include "days_private.h"

using namespace std;

namespace day11 {
    struct size {
        size_t width;
        size_t height;
    };

    size getMapSize(const vector<string>& map) {
        return size{map.at(0).size(), map.size()};
    }

    static constexpr array<pair<short, short>, 8> directions{{
        {-1, -1}, {+0, -1}, {+1, -1},
        {-1, +0}, {+1, +0},
        {-1, +1}, {+0, +1}, {+1, +1}}};

    bool isValidDirection(const vector<string>& map, const uint x, const uint y, const pair<int, int>& direction) {
        auto size = getMapSize(map);
        return !((x == 0 && direction.first == -1)
            || (x == size.width - 1 && direction.first == 1)
            || (y == 0 && direction.second == -1)
            || (y == size.height - 1 && direction.second == 1));
    }

    void runRounds(vector<string>& map, bool needsSwap(const vector<string>& map, const uint x, const uint y)) {
        auto findSwaps = [&map, &needsSwap](uint offset, uint inc) {
            auto size = getMapSize(map);
            vector<pair<int, int>> swaps;
            for (auto y = offset; y < size.height; y += inc)
                for (auto x = 0U; x < size.width; x++)
                    if (needsSwap(map, x, y))
                        swaps.emplace_back(x, y);

            return swaps;
        };

        while (true) {
            vector<future<vector<pair<int, int>>>> futures;
            futures.reserve(util::concurrency());
            for (auto offset = 0U; offset < util::concurrency(); offset++)
                futures.push_back(async(launch::async, findSwaps, offset, util::concurrency()));

            vector<pair<int, int>> swaps;
            for (auto& future : futures) {
                auto value = future.get();
                move(value.begin(), value.end(), back_inserter(swaps));
            }

            if (swaps.empty())
                return;

            for (const auto& swap : swaps)
                map.at(swap.second).at(swap.first) = map.at(swap.second).at(swap.first) == '#' ? 'L' : '#';
        }
    }

    string getAdjacentSeats(const vector<string>& map, const uint x, const uint y) {
        string seats;
        for (const auto& direction : directions)
            if (isValidDirection(map, x, y, direction))
                seats.push_back(map.at(y + direction.second).at(x + direction.first));

        return seats;
    }

    bool needsSwapDueToAdjacentSeats(const vector<string>& map, const uint x, const uint y) {
        auto seat = map.at(y).at(x);
        if (seat == '.')
            return false;

        auto adjacentSeats = getAdjacentSeats(map, x, y);
        if (seat == 'L' && adjacentSeats.find('#') == string::npos)
            return true;

        auto occupiedSeats = count(adjacentSeats.begin(), adjacentSeats.end(), '#');
        if (seat == '#' && occupiedSeats >= 4)
            return true;

        return false;
    }

    TEST_CASE("Day 11 - Part 1 from https://adventofcode.com/2020/day/11") {
        auto mapData = util::loadInputFile("day11-input.txt");

        runRounds(mapData, needsSwapDueToAdjacentSeats);
        auto result = accumulate(mapData.begin(), mapData.end(), 0U,
            [](const auto sum, const auto& line) {
                return sum + count(line.begin(), line.end(), '#');
            });

        REQUIRE(result == 2283);
    }

    bool hasOccupiedSeat(const vector<string>& map, const uint x, const uint y, const function<bool(uint& x1, uint& y1)>& move) {
        auto x1 = x;
        auto y1 = y;

        while (true) {
            if (!move(x1, y1))
                return false;

            if (map.at(y1).at(x1) == '#')
                return true;

            if (map.at(y1).at(x1) == 'L')
                return false;
        }
    }

    bool needsSwapDueToFirstVisibleSeat(const vector<string>& map, const uint x, const uint y) {
        auto seat = map.at(y).at(x);
        if (seat == '.')
            return false;

        auto occupiedSeats = 0U;
        for (const auto& direction : directions)
            if (hasOccupiedSeat(map, x, y, [&map, &direction](auto& x1, auto& y1){
                    if (!isValidDirection(map, x1, y1, direction))
                        return false;

                    x1 += direction.first;
                    y1 += direction.second;
                    return true;
                }))
                occupiedSeats++;

        if (seat == 'L' && occupiedSeats == 0)
            return true;

        if (seat == '#' && occupiedSeats >= 5)
            return true;

        return false;
    }

    TEST_CASE("Day 11 - Part 2 from https://adventofcode.com/2020/day/11#part2") {
        auto mapData = util::loadInputFile("day11-input.txt");

        runRounds(mapData, needsSwapDueToFirstVisibleSeat);
        auto result = accumulate(mapData.begin(), mapData.end(), 0U,
            [](const auto sum, const auto& line) {
                return sum + count(line.begin(), line.end(), '#');
            });

        REQUIRE(result == 2054);
    }
}
