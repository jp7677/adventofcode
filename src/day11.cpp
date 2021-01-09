#include "days_private.h"

using namespace std;

namespace day11 {
    struct size {
        int width;
        int height;
    };

    size getMapSize(const vector<string>& map) {
        return size{static_cast<int>(map.at(0).size()), static_cast<int>(map.size())};
    }

    void runRounds(vector<string>& map, bool needsSwap(const vector<string>& map, const int x, const int y)) {
        auto size = getMapSize(map);
        auto findSwaps = [&map, &size, &needsSwap](int offset, uint inc) {
            vector<pair<int, int>> swaps;
            for (auto y = offset; y < size.height; y += inc)
                for (auto x = 0; x < size.width; x++)
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

    string getAdjacentSeats(const vector<string>& map, const int x, const int y) {
        auto size = getMapSize(map);
        string seats;
        if (x > 0 && y > 0)
            seats.push_back(map.at(y - 1).at(x - 1));
        if (y > 0)
            seats.push_back(map.at(y - 1).at(x));
        if (x < size.width - 1 && y > 0)
            seats.push_back(map.at(y - 1).at(x + 1));
        if (x > 0)
            seats.push_back(map.at(y).at(x - 1));
        if (x < size.width - 1)
            seats.push_back(map.at(y).at(x + 1));
        if (x > 0 && y < size.height - 1)
            seats.push_back(map.at(y + 1).at(x - 1));
        if (y < size.height - 1)
            seats.push_back(map.at(y + 1).at(x));
        if (x < size.width - 1 && y < size.height - 1)
            seats.push_back(map.at(y + 1).at(x + 1));

        return seats;
    }

    bool needsSwapDueToAdjacentSeats(const vector<string>& map, const int x, const int y) {
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

    bool hasOccupiedSeat(const vector<string>& map, const int x, const int y, void move(int& x1, int& y1)) {
        auto x1 = x;
        auto y1 = y;
        move(x1, y1);

        auto size = getMapSize(map);
        if (x1 < 0 || y1 < 0 || x1 > size.width - 1 || y1 > size.height - 1)
            return false;

        if (map.at(y1).at(x1) == '#')
            return true;

        if (map.at(y1).at(x1) == 'L')
            return false;

        return hasOccupiedSeat(map, x1, y1, move);
    }

    bool needsSwapDueToFirstVisibleSeat(const vector<string>& map, const int x, const int y) {
        auto seat = map.at(y).at(x);
        if (seat == '.')
            return false;

        auto occupiedSeats = 0U;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1--;y1--; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ y1--; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1++;y1--; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1--; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1++; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1--;y1++; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ y1++; })) occupiedSeats++;
        if (hasOccupiedSeat(map, x, y, []([[maybe_unused]] auto x1, [[maybe_unused]] auto y1){ x1++;y1++; })) occupiedSeats++;

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
