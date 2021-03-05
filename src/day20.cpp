#include "days_private.h"

using namespace std;

namespace day00 {

    struct border {
        string top, right, bottom, left;
    };

    struct adjacentBorderCount {
        uint top, right, bottom, left;

        [[nodiscard]] uint getAdjacentCount() const {
            auto count = 0U;
            if (top > 0) count++;
            if (right > 0) count++;
            if (bottom > 0) count++;
            if (left > 0) count++;

            return count;
        }
    };

    border getTileBorder(const vector<string>& tile) {
        auto right = string();
        auto left = string();
        for (const auto& lane : tile) {
            left += lane[0];
            right += lane[lane.size() - 1];
        }
        return border{tile[0], right, tile[tile.size() - 1], left};
    }

    bool anyAdjacentBorder(const string& a, const border& b) {
        return a == b.top || a == util::reverse(b.top)
            || a == b.right || a == util::reverse(b.right)
            || a == b.bottom || a == util::reverse(b.bottom)
            || a == b.left || a == util::reverse(b.left);
    }

    TEST_CASE("Day 20 - Part 1 from https://adventofcode.com/2020/day/20") {
        auto tilesData = util::loadInputFile("day20-input.txt");

        unordered_map<uint, vector<string>> tiles;
        uint currentTile;
        for (const auto& tileData : tilesData) {
            if (tileData[0] == 'T') {
                currentTile = stoul(util::split(tileData, ' ', ':')[1]);
                tiles.emplace(currentTile, vector<string>());
            } else if (!tileData.empty())
                tiles[currentTile].emplace_back(tileData);
        }

        vector<uint> cornerTileIds;
        for (const auto& tile : tiles) {
            auto border = getTileBorder(tile.second);
            auto count = adjacentBorderCount{0, 0, 0, 0};

            for (const auto& testTile : tiles) {
                if (tile.first == testTile.first)
                    continue;

                auto testTileBorders = getTileBorder(testTile.second);
                if (anyAdjacentBorder(border.top, testTileBorders))
                    count.top++;
                if (anyAdjacentBorder(border.bottom, testTileBorders))
                    count.bottom++;
                if (anyAdjacentBorder(border.right, testTileBorders))
                    count.right++;
                if (anyAdjacentBorder(border.left, testTileBorders))
                    count.left++;
            }

            if (count.getAdjacentCount() == 2)
                cornerTileIds.emplace_back(tile.first);
        }

        auto result = accumulate(cornerTileIds.begin(), cornerTileIds.end(), 1UL, multiplies<>());

        REQUIRE(result == 7492183537913);
    }
}
