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

        struct hash {
            size_t operator() (const cube_position& a) const {
                return a.x ^ (a.y << 4) ^ (a.z << 8); // Beware there be dragons.
            }
        };
    };

    struct hypercube_position : cube_position {
        int w;

        bool operator==(const hypercube_position &a) const {
            return x == a.x && y == a.y && z == a.z && w == a.w;
        }

        hypercube_position operator+(const hypercube_position &a) const {
            return hypercube_position({{x + a.x, y + a.y, z + a.z}, w + a.w});
        }

        struct hash {
            size_t operator() (const hypercube_position& a) const {
                return a.x ^ (a.y << 4) ^ (a.z << 8) ^ (a.w << 12); // Beware there be dragons.
            }
        };
    };

    static constexpr array<int, 3> moves{{-1, 0, +1}};

    template<typename T, typename M>
    unordered_set<T, M> runCycle(const unordered_set<T, M>& activeCubes, vector<T>& neighbourDirections) {
        unordered_set<T, M> testedNeighbours;
        unordered_set<T, M> resultingActiveCubes;
        for (auto activeCube : activeCubes) {
            auto activeNeighbours = 0U;
            for (const auto& direction : neighbourDirections) {
                auto neighbour = activeCube + direction;
                if (activeCubes.find(neighbour) != activeCubes.end())
                    activeNeighbours++;

                if (testedNeighbours.find(neighbour) != testedNeighbours.end())
                    continue;

                auto activeNeighboursOfNeighbour = 0U;
                for (const auto& directionOfNeighbour : neighbourDirections) {
                    if (activeCubes.find(neighbour + directionOfNeighbour) != activeCubes.end())
                        activeNeighboursOfNeighbour++;

                    if (activeNeighboursOfNeighbour == 4)
                        break;
                }

                testedNeighbours.insert(neighbour);
                if (activeNeighboursOfNeighbour == 3)
                    resultingActiveCubes.insert(neighbour);
            }

            if (activeNeighbours == 2 || activeNeighbours == 3)
                resultingActiveCubes.insert(activeCube);
        }

        return resultingActiveCubes;
    }

    TEST_CASE("Day 17 - Part 1 https://adventofcode.com/2020/day/17") {
        auto cubeData = util::loadInputFile("day17-input.txt");

        unordered_set<cube_position, cube_position::hash> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData[i].size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.insert(cube_position({static_cast<int>(j), static_cast<int>(i), 0}));

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

        unordered_set<hypercube_position, hypercube_position::hash> activeCubes;
        for (auto i = 0U; i < cubeData.size(); i++)
            for (auto j = 0U; j < cubeData[i].size(); j++)
                if (cubeData[i][j] == '#')
                    activeCubes.insert(hypercube_position({{static_cast<int>(j), static_cast<int>(i), 0}, 0}));

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
