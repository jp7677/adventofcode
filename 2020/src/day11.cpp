#include "days_private.h"

using namespace std;

namespace day11 {
    static constexpr array<pair<short, short>, 8> directions{{
        // clang-format off
        {-1, -1}, {+0, -1}, {+1, -1},
        {-1, +0},           {+1, +0},
        {-1, +1}, {+0, +1}, {+1, +1}
        // clang-format on
    }};

    struct size {
        size_t width;
        size_t height;
    };

    bool isValidDirection(const size& size, const uint x, const uint y, const pair<int, int>& direction) {
        return (x != 0 || direction.first != -1)
            && (y != 0 || direction.second != -1)
            && (x != size.width - 1 || direction.first != 1)
            && (y != size.height - 1 || direction.second != 1);
    }

    void runRounds(vector<string>& map, const size& size, const function<bool(const vector<string>&, const struct size&, const uint, const uint)>& needsSwap) {
        while (true) {
            vector<pair<uint, uint>> swaps;
            swaps.reserve(size.width * size.height);
            for (auto y = 0U; y < size.height; y++)
                for (auto x = 0U; x < size.width; x++)
                    if (needsSwap(map, size, x, y))
                        swaps.emplace_back(x, y);

            if (swaps.empty())
                return;

            for (const auto& swap : swaps)
                map[swap.second][swap.first] = map[swap.second][swap.first] == '#' ? 'L' : '#';
        }
    }

    bool needsSwapDueToAdjacentSeats(const vector<string>& map, const size& size, const uint x, const uint y) {
        auto seat = map[y][x];
        if (seat == '.')
            return false;

        auto occupiedSeats = 0U;
        for (const auto& direction : directions) {
            if (!isValidDirection(size, x, y, direction))
                continue;

            if (map[y + direction.second][x + direction.first] == '#')
                occupiedSeats++;

            if (occupiedSeats == 1 && seat == 'L')
                return false;

            if (occupiedSeats == 4 && seat == '#')
                return true;
        }

        return seat == 'L';
    }

    TEST_CASE("Day 11 - Part 1 from https://adventofcode.com/2020/day/11") {
        auto mapData = util::loadInputFile("day11-input.txt");

        runRounds(mapData, size{mapData[0].size(), mapData.size()}, needsSwapDueToAdjacentSeats);
        auto result = accumulate(mapData.begin(), mapData.end(), 0U,
            [](const auto sum, const auto& line) {
                return sum + count(line.begin(), line.end(), '#');
            });

        REQUIRE(result == 2283);
    }

    bool hasOccupiedSeat(const vector<string>& map, const size& size, const uint x, const uint y, const pair<int, int>& direction) {
        auto x1 = x;
        auto y1 = y;

        while (true) {
            if (!isValidDirection(size, x1, y1, direction))
                return false;

            x1 += direction.first;
            y1 += direction.second;

            if (map[y1][x1] == '#')
                return true;

            if (map[y1][x1] == 'L')
                return false;
        }
    }

    bool needsSwapDueToFirstVisibleSeat(const vector<string>& map, const size& size, const uint x, const uint y) {
        auto seat = map[y][x];
        if (seat == '.')
            return false;

        auto occupiedSeats = 0U;
        for (const auto& direction : directions) {
            if (hasOccupiedSeat(map, size, x, y, direction))
                occupiedSeats++;

            if (occupiedSeats == 1 && seat == 'L')
                return false;

            if (occupiedSeats == 5 && seat == '#')
                return true;
        }

        return seat == 'L';
    }

    TEST_CASE("Day 11 - Part 2 from https://adventofcode.com/2020/day/11#part2") {
        auto mapData = util::loadInputFile("day11-input.txt");

        runRounds(mapData, size{mapData[0].size(), mapData.size()}, needsSwapDueToFirstVisibleSeat);
        auto result = accumulate(mapData.begin(), mapData.end(), 0U,
            [](const auto sum, const auto& line) {
                return sum + count(line.begin(), line.end(), '#');
            });

        REQUIRE(result == 2054);
    }
}
