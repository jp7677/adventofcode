#include "days_private.h"

using namespace std;

namespace day00 {
    struct border {
        string top, right, bottom, left;
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
        for (const auto& tileData : tilesData)
            if (tileData[0] == 'T') {
                currentTile = stoul(util::split(tileData, ' ', ':')[1]);
                tiles.emplace(currentTile, vector<string>());
            } else if (!tileData.empty())
                tiles[currentTile].emplace_back(tileData);

        auto result = accumulate(tiles.begin(), tiles.end(), 1UL,
            [&tiles](const auto product, const auto& tile) {
                auto border = getTileBorder(tile.second);
                auto adjacentBorders = count_if(tiles.begin(), tiles.end(),
                    [&tile, &border](const auto& other) {
                        if (tile.first == other.first)
                            return false;

                        auto otherBorder = getTileBorder(other.second);
                        return anyAdjacentBorder(border.top, otherBorder)
                            || anyAdjacentBorder(border.bottom, otherBorder)
                            || anyAdjacentBorder(border.right, otherBorder)
                            || anyAdjacentBorder(border.left, otherBorder);
                    });

                return adjacentBorders == 2 ? product * tile.first : product;
            });

        REQUIRE(result == 7492183537913);
    }
}
