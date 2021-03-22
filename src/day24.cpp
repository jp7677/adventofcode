#include "days_private.h"

using namespace std;

namespace day24 {
    enum direction { e, se, sw, w, nw, ne };

    set<pair<int, int>> loadBlackTiles() {
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

        set<pair<int, int>> flippedTiles;
        for (const auto& tileStep : tileSteps) {
            pair<int, int> tile(0, 0);
            for (const auto& tileDirection : tileStep) {
                if (tileDirection == direction::e)
                    tile.first++;
                else if (tileDirection == direction::w)
                    tile.first--;
                else if (tileDirection == direction::ne) {
                    tile.first = tile.second % 2 == 0 ? tile.first + 1 : tile.first;
                    tile.second++;
                } else if (tileDirection == direction::nw) {
                    tile.first = tile.second % 2 == 0 ? tile.first : tile.first - 1;
                    tile.second++;
                } else if (tileDirection == direction::se) {
                    tile.first = tile.second % 2 == 0 ? tile.first + 1 : tile.first;
                    tile.second--;
                } else if (tileDirection == direction::sw) {
                    tile.first = tile.second % 2 == 0 ? tile.first : tile.first - 1;
                    tile.second--;
                }
            }

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

    set<pair<int, int>> getAdjacentTiles(pair<int, int> tile) {
        auto even = tile.second % 2 == 0;
        set<pair<int, int>> adjacentTiles;
        adjacentTiles.insert({tile.first + 1, tile.second});
        adjacentTiles.insert({tile.first - 1, tile.second});
        adjacentTiles.insert({even ? tile.first + 1 : tile.first, tile.second + 1});
        adjacentTiles.insert({even ? tile.first : tile.first - 1, tile.second + 1});
        adjacentTiles.insert({even ? tile.first + 1 : tile.first, tile.second - 1});
        adjacentTiles.insert({even ? tile.first : tile.first - 1, tile.second - 1});

        return adjacentTiles;
    }

    void flipTiles(set<pair<int, int>>& tiles) {
        set<pair<int, int>> whiteTiles;
        set<pair<int, int>> blackTiles(tiles);
        for (const auto& blackTile : blackTiles)
            for (const auto& adjacentTile : getAdjacentTiles(blackTile))
                if (blackTiles.find(adjacentTile) == blackTiles.end())
                    whiteTiles.insert(adjacentTile);

        for (const auto& blackTile : blackTiles) {
            auto adjacentBlackTiles = 0U;
            for (const auto& adjacentTile : getAdjacentTiles(blackTile))
                if (blackTiles.find(adjacentTile) != blackTiles.end())
                    adjacentBlackTiles++;

            if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2)
                tiles.erase(blackTile);
        }

        for (const auto& whiteTile : whiteTiles) {
            auto adjacentBlackTiles = 0U;
            for (const auto& adjacentTile : getAdjacentTiles(whiteTile))
                if (blackTiles.find(adjacentTile) != blackTiles.end())
                    adjacentBlackTiles++;

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
