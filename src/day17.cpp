#include "days_private.h"

using namespace std;

namespace day17 {
    static constexpr array<int, 3> moves{{-1, 0, +1}};

    struct cube_position {
        int x,y,z;

        cube_position(int x, int y, int z = 0) {
            this->x = x;
            this->y = y;
            this->z = z;
        }

        bool operator==(const cube_position &a) const {
            return x == a.x && y == a.y && z == a.z;
        }

        cube_position operator+(const cube_position &a) const {
            return cube_position(x + a.x, y + a.y, z + a.z);
        }

        struct hash {
            size_t operator() (const cube_position& a) const {
                return a.x ^ (a.y << 4) ^ (a.z << 8); // Beware there be dragons.
            }
        };
    };

    struct hypercube_position : cube_position {
        int w;

        hypercube_position(int x, int y, int z = 0, int w = 0) : cube_position(x, y, z) {
            this->w = w;
        }

        bool operator==(const hypercube_position &a) const {
            return x == a.x && y == a.y && z == a.z && w == a.w;
        }

        hypercube_position operator+(const hypercube_position &a) const {
            return hypercube_position(x + a.x, y + a.y, z + a.z, w + a.w);
        }

        struct hash {
            size_t operator() (const hypercube_position& a) const {
                return a.x ^ (a.y << 4) ^ (a.z << 8) ^ (a.w << 12); // Beware there be dragons.
            }
        };
    };

    template<typename T, typename H>
    unordered_set<T, H> loadActiveCubes() {
        auto cubeData = util::loadInputFile("day17-input.txt");

        unordered_set<T, H> activeCubes;
        for (auto x = 0U; x < cubeData.size(); x++)
            for (auto y = 0U; y < cubeData[x].size(); y++)
                if (cubeData[x][y] == '#')
                    activeCubes.insert(T(static_cast<int>(y), static_cast<int>(x)));

        return activeCubes;
    }

    template<typename T, typename H>
    unordered_set<T, H> runCycle(const unordered_set<T, H>& activeCubes, const vector<T>& directions) {
        unordered_set<T, H> resultingActiveCubes;
        for (auto activeCube : activeCubes) {
            auto activeNeighbours = accumulate(directions.begin(), directions.end(), 0U,
                [&activeCubes, &activeCube](const auto sum, const auto& direction){
                    return sum <= 3 && activeCubes.find(activeCube + direction) != activeCubes.end() ? sum + 1 : sum;
                });

            if (activeNeighbours == 2 || activeNeighbours == 3)
                resultingActiveCubes.insert(activeCube);
        }

        unordered_set<T, H> testedNeighbours;
        for (auto activeCube : activeCubes) {
            for (const auto& direction : directions) {
                auto neighbour = activeCube + direction;
                if (testedNeighbours.find(neighbour) != testedNeighbours.end())
                    continue;

                auto activeNeighboursOfNeighbour = accumulate(directions.begin(), directions.end(), 0U,
                    [&activeCubes, &neighbour](const auto sum, const auto& direction){
                        return sum <= 3 && activeCubes.find(neighbour + direction) != activeCubes.end() ? sum + 1 : sum;
                    });

                testedNeighbours.insert(neighbour);
                if (activeNeighboursOfNeighbour == 3)
                    resultingActiveCubes.insert(neighbour);
            }
        }

        return resultingActiveCubes;
    }

    template<typename T, typename H>
    uint runCycles(unordered_set<T, H>& activeCubes, const vector<T>& directions) {
        for (auto i = 0U; i < 6; i++)
            activeCubes = runCycle(activeCubes, directions);

        return activeCubes.size();
    }

    TEST_CASE("Day 17 - Part 1 https://adventofcode.com/2020/day/17") {
        auto activeCubes = loadActiveCubes<cube_position, cube_position::hash>();

        vector<cube_position> directions;
        for (const auto x : moves)
            for (const auto y : moves)
                for (const auto z : moves)
                    if (x != 0 || y != 0 || z != 0)
                        directions.emplace_back(cube_position(x, y, z));

        auto result = runCycles(activeCubes, directions);

        REQUIRE(result == 391);
    }

    TEST_CASE("Day 17 - Part 2 https://adventofcode.com/2020/day/17#part2") {
        auto activeCubes = loadActiveCubes<hypercube_position, hypercube_position::hash>();

        vector<hypercube_position> directions;
        for (const auto x : moves)
            for (const auto y : moves)
                for (const auto z : moves)
                    for (const auto w : moves)
                        if (x != 0 || y != 0 || z != 0 || w != 0)
                            directions.emplace_back(hypercube_position(x, y, z, w));

        auto result = runCycles(activeCubes, directions);

        REQUIRE(result == 2264);
    }
}
