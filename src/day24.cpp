#include "days_private.h"

using namespace std;

namespace day24 {
    enum direction { e, se, sw, w, nw, ne };

    TEST_CASE("Day 24 - Part 1 from https://adventofcode.com/2020/day/24") {
        auto floorData = util::loadInputFile("day24-input.txt");

        vector<vector<direction>> tileSteps;
        transform(floorData.begin(), floorData.end(), back_inserter(tileSteps),
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

        auto result = flippedTiles.size();

        REQUIRE(result == 373);
    }
}
