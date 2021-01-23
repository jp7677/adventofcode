#include "days_private.h"

using namespace std;

namespace day17 {
    struct cube_position {
        int x;
        int y;
        int z;

        bool operator==(const cube_position &a) const {
            return x == a.x && y == a.y && z == a.z;
        }

        cube_position operator+(const cube_position &a) const {
            return cube_position({x + a.x, y + a.y, z + a.z});
        }
    };

    struct hypercube_position : cube_position {
        int w;

        bool operator==(const hypercube_position &a) const {
            return x == a.x && y == a.y && z == a.z && w == a.w;
        }

        hypercube_position operator+(const hypercube_position &a) const {
            return hypercube_position({{x + a.x, y + a.y, z + a.z}, w + a.w});
        }
    };

    static constexpr array<int, 3> moves{{-1, 0, +1}};

    template<typename T>
    vector<T> runCycle(const vector<T>& activeCubes, vector<T>& neighbourDirections) {
        vector<T> resultingActiveCubes;
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

        vector<cube_position> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData[i].size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.emplace_back(cube_position({static_cast<int>(j), static_cast<int>(i), 0}));

        vector<cube_position> directions;
        for (const auto x : moves)
            for (const auto y : moves)
                for (const auto z : moves)
                    if (!(x == 0 && y == 0 && z == 0))
                        directions.emplace_back(cube_position({x, y, z}));

        for (auto i = 0U; i < 6; i++)
            activeCubes = runCycle(activeCubes, directions);

        auto result = activeCubes.size();

        REQUIRE(result == 391);
    }

    TEST_CASE("Day 17 - Part 2 https://adventofcode.com/2020/day/17#part2") {
        auto cubeData = util::loadInputFile("day17-input.txt");

        vector<hypercube_position> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData[i].size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.emplace_back(hypercube_position({{static_cast<int>(j), static_cast<int>(i), 0}, 0}));

        vector<hypercube_position> directions;
        for (const auto x : moves)
            for (const auto y : moves)
                for (const auto z : moves)
                    for (const auto w : moves)
                        if (!(x == 0 && y == 0 && z == 0 && w == 0))
                            directions.emplace_back(hypercube_position({{x, y, z}, w}));

        for (auto i = 0U; i < 6; i++)
            activeCubes = runCycle(activeCubes, directions);

        auto result = activeCubes.size();

        REQUIRE(result == 2264);
    }
}
