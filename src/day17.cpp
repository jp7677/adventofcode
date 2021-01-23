#include "days_private.h"

using namespace std;

namespace day17 {
    struct position {
        int x;
        int y;
        int z;

        bool operator==(const position &a) const {
            return x == a.x && y == a.y && z == a.z;
        }

        position operator+(const position &a) const {
            return position({x + a.x, y + a.y, z + a.z});
        }
    };

    static constexpr array<position, 26> neighbourDirections{{
        {-1, -1, -1}, {+0, -1, -1}, {+1, -1, -1},
        {-1, +0, -1}, {+0, +0, -1}, {+1, +0, -1},
        {-1, +1, -1}, {+0, +1, -1}, {+1, +1, -1},
        {-1, -1, +0}, {+0, -1, +0}, {+1, -1, +0},
        {-1, +0, +0}, {+1, +0, +0},
        {-1, +1, +0}, {+0, +1, +0}, {+1, +1, +0},
        {-1, -1, +1}, {+0, -1, +1}, {+1, -1, +1},
        {-1, +0, +1}, {+0, +0, +1}, {+1, +0, +1},
        {-1, +1, +1}, {+0, +1, +1}, {+1, +1, +1}}};

    vector<position> runCycle(const vector<position>& activeCubes) {
        vector<position> resultingActiveCubes;
        for (auto activeCube : activeCubes) {
            auto activeNeighbours = 0U;
            for (const auto& direction : neighbourDirections) {
                auto neighbour = activeCube + direction;
                if (find(activeCubes.begin(), activeCubes.end(), neighbour) != activeCubes.end())
                    activeNeighbours++;

                if (find(resultingActiveCubes.begin(), resultingActiveCubes.end(), neighbour) != resultingActiveCubes.end())
                    continue;

                auto activeNeighboursOfNeighbour = 0U;
                for (const auto& directionOfNeighbour : neighbourDirections) {
                    if (find(activeCubes.begin(), activeCubes.end(), neighbour + directionOfNeighbour) != activeCubes.end())
                        activeNeighboursOfNeighbour++;

                    if (activeNeighboursOfNeighbour == 4)
                        break;
                }

                if (activeNeighboursOfNeighbour == 3)
                    resultingActiveCubes.emplace_back(neighbour);
            }

            if ((activeNeighbours == 2 || activeNeighbours == 3)
                && find(resultingActiveCubes.begin(), resultingActiveCubes.end(), activeCube) == resultingActiveCubes.end())
                resultingActiveCubes.emplace_back(activeCube);
        }

        return resultingActiveCubes;
    }

    TEST_CASE("Day 17 - Part 1 https://adventofcode.com/2020/day/17") {
        auto cubeData = util::loadInputFile("day17-input.txt");

        vector<position> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData[i].size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.emplace_back(position({static_cast<int>(j), static_cast<int>(i), 0}));

        for (auto i = 0U; i < 6; i++)
            activeCubes = runCycle(activeCubes);

        auto result = activeCubes.size();

        REQUIRE(result == 391);
    }
}
