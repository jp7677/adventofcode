#include "days_private.h"

using namespace std;

namespace day17 {
    struct position {
        int x;
        int y;
        int z;

        bool operator==(const position& a) const {
            return x == a.x && y == a.y && z == a.z;
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
        vector<position> affectedCubes(activeCubes);
        for (auto activeCube : activeCubes)
            for (const auto &direction : neighbourDirections) {
                auto neighbour = position({activeCube.x + direction.x, activeCube.y + direction.y, activeCube.z + direction.z});
                if (find(affectedCubes.begin(), affectedCubes.end(), neighbour) == affectedCubes.end())
                    affectedCubes.push_back(neighbour);
            }

        for (auto affectedCube : affectedCubes) {
            auto isActive = find(activeCubes.begin(), activeCubes.end(), affectedCube) != activeCubes.end();
            auto activeNeighbours = 0U;
            for (const auto &direction : neighbourDirections) {
                auto neighbour = position({affectedCube.x + direction.x, affectedCube.y + direction.y, affectedCube.z + direction.z});
                if (find(activeCubes.begin(), activeCubes.end(), neighbour) != activeCubes.end())
                    activeNeighbours++;
            }

            if (isActive && (activeNeighbours == 2 || activeNeighbours == 3))
                if (find(resultingActiveCubes.begin(), resultingActiveCubes.end(), affectedCube) == resultingActiveCubes.end())
                    resultingActiveCubes.push_back(affectedCube);

            if (!isActive && activeNeighbours == 3)
                if (find(resultingActiveCubes.begin(), resultingActiveCubes.end(), affectedCube) == resultingActiveCubes.end())
                    resultingActiveCubes.push_back(affectedCube);
        }

        return resultingActiveCubes;
    }

    TEST_CASE("Day 17 - Part 1 https://adventofcode.com/2020/day/17") {
        auto cubeData = util::loadInputFile("day17-input.txt");

        vector<position> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData.at(i).size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.push_back(position({static_cast<int>(j), static_cast<int>(i), 0}));

        for (auto i = 0U; i < 6; i++)
            activeCubes = runCycle(activeCubes);

        auto result = activeCubes.size();

        REQUIRE(result == 391);
    }
}
