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

    uint findTopLeftTile(const unordered_map<uint, vector<string>>& tiles) {
        auto it = find_if(tiles.begin(), tiles.end(),
            [&tiles](const auto& tile){
                auto border = getTileBorder(tile.second);
                return find_if(tiles.begin(), tiles.end(),
                        [&tile, &border](const auto& other){
                            auto otherBorder = getTileBorder(other.second);
                            return tile.first != other.first && anyAdjacentBorder(border.top, otherBorder);
                        }) == tiles.end()
                    && find_if(tiles.begin(), tiles.end(),
                        [&tile, &border](const auto& other){
                            auto otherBorder = getTileBorder(other.second);
                            return tile.first != other.first && anyAdjacentBorder(border.left, otherBorder);
                        }) == tiles.end();
            });

        // the given data sets contain a top left corner, we should actually just rotate the first corner tile found here to become a top left corner
        return it != tiles.end() ? (*it).first : throw runtime_error("invalid data");
    }

    vector<string> orientate(const vector<string>& tile, const bool mirror, const uint times) {
        vector<string> mirroredTile(tile);
        if (mirror)
            transform(mirroredTile.begin(), mirroredTile.end(), mirroredTile.begin(),
                [](const auto& line) {
                    return util::reverse(line);
                });

        vector<string> rotatedTile(mirroredTile);
        for (auto t = 0U; t < times; t++) {
            for (auto y = 0U; y < mirroredTile.size(); y++)
                for (auto x = 0U; x < mirroredTile[0].size(); x++)
                    rotatedTile[x][y] = mirroredTile[mirroredTile[0].size() - 1 - y][x];

            mirroredTile = rotatedTile;
        }

        return rotatedTile;
    }

    vector<string> orientateToLeftBoarderIfNeeded(const vector<string>& tile, const string& leftBoarder) {
        auto boarder = getTileBorder(tile);
        if (boarder.top == leftBoarder)
            return orientate(tile, true, 3);
        else if (boarder.right == leftBoarder)
            return orientate(tile, true, 0);
        else if (boarder.bottom == leftBoarder)
            return orientate(tile, false, 1);
        else if (util::reverse(boarder.top) == leftBoarder)
            return orientate(tile, false, 3);
        else if (util::reverse(boarder.right) == leftBoarder)
            return orientate(tile, false, 2);
        else if (util::reverse(boarder.bottom) == leftBoarder)
            return orientate(tile, true, 1);
        else if (util::reverse(boarder.left) == leftBoarder)
            return orientate(tile, true, 2);

        return tile;
    }

    vector<string> orientateToTopBoarderIfNeeded(const vector<string>& tile, const string& topBoarder) {
        auto boarder = getTileBorder(tile);
        if (boarder.right == topBoarder)
            return orientate(tile, false, 3);
        else if (boarder.bottom == topBoarder)
            return orientate(tile, true, 2);
        else if (boarder.left == topBoarder)
            return orientate(tile, true, 3);
        else if (util::reverse(boarder.top) == topBoarder)
            return orientate(tile, true, 0);
        else if (util::reverse(boarder.right) == topBoarder)
            return orientate(tile, true, 1);
        else if (util::reverse(boarder.bottom) == topBoarder)
            return orientate(tile, false, 2);
        else if (util::reverse(boarder.left) == topBoarder)
            return orientate(tile, false, 1);

        return tile;
    }

    vector<string> puzzleImage(const unordered_map<uint, vector<string>>& tiles) {
        auto topLeftCornerTileId = findTopLeftTile(tiles);

        vector<string> image;
        for (auto p = 1U; p < tiles.at(topLeftCornerTileId).size() - 1; p++)
            image.push_back(tiles.at(topLeftCornerTileId)[p].substr(1, tiles.at(topLeftCornerTileId)[p].size() - 2));

        vector<uint> foundTiles(tiles.size());
        foundTiles[0] = topLeftCornerTileId;
        auto topLeftCornerTileBorder = getTileBorder(tiles.at(topLeftCornerTileId));
        auto firstInRowBottomBorder = topLeftCornerTileBorder.bottom;
        auto leftBorder = topLeftCornerTileBorder.right;
        for (auto i = 1U; i <= tiles.size(); i++ ) {
            for (const auto& other : tiles) {
                if (find(foundTiles.begin(), foundTiles.end(), other.first) != foundTiles.end())
                    continue;

                if (anyAdjacentBorder(leftBorder, getTileBorder(other.second))) {
                    foundTiles[i] = other.first;
                    auto orientedTile = orientateToLeftBoarderIfNeeded(other.second, leftBorder);
                    leftBorder = getTileBorder(orientedTile).right;
                    for (auto p = 1U; p < orientedTile.size() - 1; p++)
                        image[image.size() - orientedTile.size() + 1 + p] += orientedTile[p].substr(1, orientedTile[p].size() - 2);

                    break;
                }
            }

            if (foundTiles[i] != 0)
                continue;

            for (const auto& other : tiles) {
                if (find(foundTiles.begin(), foundTiles.end(), other.first) != foundTiles.end())
                    continue;

                if (anyAdjacentBorder(firstInRowBottomBorder, getTileBorder(other.second))) {
                    foundTiles[i] = other.first;
                    auto orientedTile = orientateToTopBoarderIfNeeded(tiles.at(other.first), firstInRowBottomBorder);
                    auto orientedTileBorder = getTileBorder(orientedTile);
                    firstInRowBottomBorder = orientedTileBorder.bottom;
                    leftBorder = orientedTileBorder.right;
                    for (auto p = 1U; p < orientedTile.size() - 1; p++)
                        image.push_back(orientedTile[p].substr(1, orientedTile[p].size() - 2));

                    break;
                }
            }
        }

        return image;
    }

    constexpr uint seaMonsterWidth = 21;
    constexpr uint seaMonsterHeight = 3;

    bool hasSeaMonster(const vector<string>& image, const uint x, const uint y) {
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
            // rotate only works for the given data sets, we should actually also mirror when searching for the correct orientation
            image = orientate(image, false, 1);
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
