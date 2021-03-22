#include "days_private.h"

using namespace std;

namespace day24 {
    enum direction { e, se, sw, w, nw, ne };

    struct hash {
        size_t operator() (const pair<int, int>& a) const {
            return a.first ^ (a.second << 4); // Beware there be dragons.
        }
    };

    unordered_set<pair<int, int>, hash> loadBlackTiles() {
        auto floor = util::loadInputFile("day24-input.txt");

        vector<vector<direction>> tileSteps;
        transform(floor.begin(), floor.end(), back_inserter(tileSteps),
            [](const auto& tile) {
                vector<direction> directions;
                for (auto i = 0U; i < tile.size(); i++) {
                    if (tile[i] == 'e')
                        directions.push_back(direction::e);
                    else if (tile[i] == 'w')
                        directions.push_back(direction::w);
                    else if (tile[i] == 'n' && tile[i + 1] == 'e')
                        directions.push_back(direction::ne);
                    else if (tile[i] == 'n' && tile[i + 1] == 'w')
                        directions.push_back(direction::nw);
                    else if (tile[i] == 's' && tile[i + 1] == 'e')
                        directions.push_back(direction::se);
                    else if (tile[i] == 's' && tile[i + 1] == 'w')
                        directions.push_back(direction::sw);

                    if (tile[i] == 'n' || tile[i] == 's')
                        i++;
                }
                return directions;
            });

        unordered_set<pair<int, int>, hash> flippedTiles;
        for (const auto& tileStep : tileSteps) {
            auto tile = accumulate(tileStep.begin(), tileStep.end(), pair<int, int>(0, 0),
                [](const auto tile, const auto& tileDirection){
                    switch (tileDirection) {
                        case direction::e : return pair<int, int>(tile.first + 1, tile.second);
                        case direction::w : return pair<int, int>(tile.first - 1, tile.second);
                        case direction::ne: return pair<int, int>(tile.second % 2 == 0 ? tile.first + 1 : tile.first, tile.second + 1);
                        case direction::nw: return pair<int, int>(tile.second % 2 == 0 ? tile.first : tile.first - 1, tile.second + 1);
                        case direction::se: return pair<int, int>(tile.second % 2 == 0 ? tile.first + 1 : tile.first, tile.second - 1);
                        case direction::sw: return pair<int, int>(tile.second % 2 == 0 ? tile.first : tile.first - 1, tile.second - 1);
                        default: throw runtime_error("invalid data");
                    }
                });

            if (flippedTiles.find(tile) == flippedTiles.end())
                flippedTiles.insert(tile);
            else
                flippedTiles.erase(tile);
        }

        return flippedTiles;
    }

    TEST_CASE("Day 24 - Part 1 from https://adventofcode.com/2020/day/24") {
        auto blackTiles = loadBlackTiles();

        auto result = blackTiles.size();

        REQUIRE(result == 373);
    }

    unordered_set<pair<int, int>, hash> getAdjacentTiles(pair<int, int> tile) {
        auto even = tile.second % 2 == 0;
        return unordered_set<pair<int, int>, hash>{
            {tile.first + 1, tile.second},
            {tile.first - 1, tile.second},
            {even ? tile.first + 1 : tile.first, tile.second + 1},
            {even ? tile.first : tile.first - 1, tile.second + 1},
            {even ? tile.first + 1 : tile.first, tile.second - 1},
            {even ? tile.first : tile.first - 1, tile.second - 1}};
    }

    void flipTiles(unordered_set<pair<int, int>, hash>& tiles) {
        unordered_set<pair<int, int>, hash> whiteTiles;
        unordered_set<pair<int, int>, hash> blackTiles(tiles);
        for (const auto& blackTile : blackTiles) {
            auto adjacentBlackTiles = 0U;
            auto adjacentTiles = getAdjacentTiles(blackTile);
            for (const auto& adjacentTile : getAdjacentTiles(blackTile))
                if (blackTiles.find(adjacentTile) == blackTiles.end())
                    whiteTiles.insert(adjacentTile);
                else
                    adjacentBlackTiles++;

            if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2)
                tiles.erase(blackTile);
        }

        for (const auto& whiteTile : whiteTiles) {
            auto adjacentTiles = getAdjacentTiles(whiteTile);
            auto adjacentBlackTiles = count_if(adjacentTiles.begin(), adjacentTiles.end(),
                [&blackTiles](const auto& adjacentTile){
                    return blackTiles.find(adjacentTile) != blackTiles.end();
                });

            if (adjacentBlackTiles == 2)
                tiles.insert(whiteTile);
        }
    }

    TEST_CASE("Day 24 - Part 2 from https://adventofcode.com/2020/day/24#part2") {
        auto blackTiles = loadBlackTiles();

        for (auto i = 0; i < 100; i++)
            flipTiles(blackTiles);

        auto result = blackTiles.size();

        REQUIRE(result == 3917);
    }
}
