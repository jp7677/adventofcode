#include "days_private.h"

using namespace std;

namespace day20 {
    struct border {
        string top, right, bottom, left;
    };

    unordered_map<uint, vector<string>> loadTiles() {
        auto tilesData = util::loadInputFile("day20-input.txt");

        unordered_map<uint, vector<string>> tiles;
        uint currentTile;
        for (const auto& tileData : tilesData)
            if (tileData[0] == 'T') {
                currentTile = stoul(util::split(tileData, ' ', ':')[1]);
                tiles.emplace(currentTile, vector<string>());
            } else if (!tileData.empty())
                tiles[currentTile].emplace_back(tileData);

        return tiles;
    }

    border getTileBorder(const vector<string>& tile) {
        auto right = string();
        auto left = string();
        for (const auto& lane : tile) {
            left += lane[0];
            right += lane[lane.size() - 1];
        }
        return border{tile[0], right, tile[tile.size() - 1], left};
    }

    bool anyAdjacentBorder(const string& borderLane, const border& otherBorder) {
        return borderLane == otherBorder.top || borderLane == util::reverse(otherBorder.top)
            || borderLane == otherBorder.right || borderLane == util::reverse(otherBorder.right)
            || borderLane == otherBorder.bottom || borderLane == util::reverse(otherBorder.bottom)
            || borderLane == otherBorder.left || borderLane == util::reverse(otherBorder.left);
    }

    TEST_CASE("Day 20 - Part 1 from https://adventofcode.com/2020/day/20") {
        auto tiles = loadTiles();

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

    uint findTopLeftTile(unordered_map<uint, vector<string>>& tiles) {
        for (const auto& tile : tiles) {
            auto border = getTileBorder(tile.second);

            bool top = false;
            bool left = false;
            for (const auto& other : tiles) {
                if (tile.first == other.first)
                    continue;

                auto otherBorder = getTileBorder(other.second);
                if (anyAdjacentBorder(border.top, otherBorder))
                    top = true;
                if (anyAdjacentBorder(border.left, otherBorder))
                    left  = true;
            }

            if (!top && !left)
                return tile.first; // TODO: only works for the given data sets, we should actually just rotate the first corner tile found here to become the top left corner
        }

        throw runtime_error("invalid data");
    }

    void mirror(vector<string>& tile) {
        transform(tile.begin(), tile.end(), tile.begin(),
            [](const auto& line) {
                return util::reverse(line);
            });
    }

    void rotate90degrees(vector<string>& tile, const uint times = 1) {
        vector<string> rotatedTile(tile);
        for (auto t = 0U; t < times; t++) {
            for (auto y = 0U; y < tile.size(); y++)
                for (auto x = 0U; x < tile[y].size(); x++)
                    rotatedTile[x][y] = tile[tile[y].size() - 1 - y][x];

            tile = rotatedTile;
        }
    }

    void orientateToLeftBoarderIfNeeded(vector<string>& tile, const string& leftBoarder) {
        auto boarder = getTileBorder(tile);
        if (boarder.left == leftBoarder)
            return;

        if (boarder.top == leftBoarder) {
            mirror(tile);
            rotate90degrees(tile, 3);
        } else if (boarder.right == leftBoarder) {
            mirror(tile);
        } else if (boarder.bottom == leftBoarder) {
            rotate90degrees(tile);
        } else if (util::reverse(boarder.top) == leftBoarder) {
            rotate90degrees(tile, 3);
        } else if (util::reverse(boarder.right) == leftBoarder) {
            rotate90degrees(tile, 2);
        } else if (util::reverse(boarder.bottom) == leftBoarder) {
            mirror(tile);
            rotate90degrees(tile);
        } else if (util::reverse(boarder.left) == leftBoarder) {
            mirror(tile);
            rotate90degrees(tile, 2);
        }
    }

    void orientateToTopBoarderIfNeeded(vector<string>& tile, const string& topBoarder) {
        auto boarder = getTileBorder(tile);
        if (boarder.top == topBoarder)
            return;

        if (boarder.right == topBoarder) {
            rotate90degrees(tile, 3);
        } else if (boarder.bottom == topBoarder) {
            mirror(tile);
            rotate90degrees(tile, 2);
        } else if (boarder.left == topBoarder) {
            mirror(tile);
            rotate90degrees(tile, 3);
        } else if (util::reverse(boarder.top) == topBoarder) {
            mirror(tile);
        } else if (util::reverse(boarder.right) == topBoarder) {
            mirror(tile);
            rotate90degrees(tile);
        } else if (util::reverse(boarder.bottom) == topBoarder) {
            rotate90degrees(tile, 2);
        } else if (util::reverse(boarder.left) == topBoarder) {
            rotate90degrees(tile);
        }
    }

    vector<string> puzzleImage(unordered_map<uint, vector<string>>& tiles) {
        auto topLeftCornerTileId = findTopLeftTile(tiles);

        vector<string> image;
        for (auto p = 1U; p < tiles[topLeftCornerTileId].size() - 1; p++)
            image.push_back(tiles[topLeftCornerTileId][p].substr(1, tiles[topLeftCornerTileId][p].size() - 2));

        vector<uint> foundTiles(tiles.size());
        foundTiles[0] = topLeftCornerTileId;
        auto firstInRowId = topLeftCornerTileId;
        for (auto i = 1U; i <= tiles.size(); i++ ) {
            auto last = foundTiles[i - 1];
            auto leftBorder = getTileBorder(tiles[last]).right;
            for (const auto& other : tiles) {
                if (count_if(foundTiles.begin(), foundTiles.end(),
                    [&other](const auto& pos) {
                        return pos == other.first;
                    }))
                    continue;

                if (anyAdjacentBorder(leftBorder, getTileBorder(other.second))) {
                    orientateToLeftBoarderIfNeeded(tiles[other.first], leftBorder);
                    foundTiles[i] = other.first;
                    for (auto p = 1U; p < other.second.size() - 1; p++)
                        image[image.size() - other.second.size() + 1 + p] += other.second[p].substr(1, other.second[p].size() - 2);

                    break;
                }
            }

            if (foundTiles[i] != 0)
                continue;

            auto bottomBorder = getTileBorder(tiles[firstInRowId]).bottom;
            for (const auto& other : tiles) {
                if (count_if(foundTiles.begin(), foundTiles.end(),
                    [&other](const auto& pos) {
                        return pos == other.first;
                    }))
                    continue;

                if (anyAdjacentBorder(bottomBorder, getTileBorder(other.second))) {
                    orientateToTopBoarderIfNeeded(tiles[other.first], bottomBorder);
                    firstInRowId = other.first;
                    foundTiles[i] = other.first;
                    for (auto p = 1U; p < other.second.size() - 1; p++)
                        image.push_back(other.second[p].substr(1, other.second[p].size() - 2));

                    break;
                }
            }
        }

        return image;
    }

    constexpr uint seaMonsterWidth = 21;
    constexpr uint seaMonsterHeight = 3;

    bool hasSeaMonster(vector<string>& image, uint x, uint y) {
        array<string, 3> water;
        water[0] = image[y].substr(x, seaMonsterWidth);
        water[1] = image[y + 1].substr(x, seaMonsterWidth);
        water[2] = image[y + 2].substr(x, seaMonsterWidth);

        return water[0][18] == '#'
            && water[1][0] == '#' && water[1][5] == '#' && water[1][6] == '#' && water[1][11] == '#' && water[1][12] == '#' && water[1][17] == '#' && water[1][18] == '#' && water[1][19] == '#'
            && water[2][1] == '#' && water[2][4] == '#' && water[2][7] == '#' && water[2][10] == '#' && water[2][13] == '#' && water[2][16] == '#';
    }

    TEST_CASE("Day 20 - Part 2 from https://adventofcode.com/2020/day/20#part2") {
        auto tiles = loadTiles();

        auto image = puzzleImage(tiles);

        auto numberOfSeaMonsters = 0U;
        while (numberOfSeaMonsters == 0) {
            rotate90degrees(image); // TODO: only works for the given data sets, we should also mirror after three attempts and bail out if none was found
            for (auto y = 0U; y < image.size() - seaMonsterHeight; y++)
                for (auto x = 0U; x < image[0].size() - seaMonsterWidth; x++)
                    if (hasSeaMonster(image, x, y))
                        numberOfSeaMonsters++;
        }

        auto waterRoughness = accumulate(image.begin(), image.end(), 0U,
            [](const auto sum, const auto& line){
                return sum + count_if(line.begin(), line.end(),
                    [](const auto& character){
                        return character == '#';
                    });
            });

        auto result = waterRoughness - (numberOfSeaMonsters * 15);

        REQUIRE(result == 2323);
    }
}
