#include "days_private.h"

using namespace std;

namespace day12 {
    vector<pair<char, int>> loadInstructions() {
        auto instructionsData = util::loadInputFile("day12-input.txt");

        vector<pair<char, int>> instructions;
        transform(instructionsData.begin(), instructionsData.end(), back_inserter(instructions),
            [](const auto& line) {
                return make_pair(line[0], stoi(line.substr(1)));
            });

        return instructions;
    }

    struct position {
        int x, y;

        [[nodiscard]] uint getManhattenDistance() const {
            return abs(x) + abs(y);
        }
    };

    struct ship : position {
        int direction;
    };

    void move(position& pos, const char direction, const int steps) {
        switch (direction) {
            // clang-format off
            case 'E': pos.x += steps; return;
            case 'W': pos.x -= steps; return;
            case 'N': pos.y += steps; return;
            case 'S': pos.y -= steps; return;
            // clang-format on
            default:
                throw runtime_error("invalid data");
        }
    }

    void move(position& pos, const int direction, const int steps) {
        auto normalized = direction % 360;
        if (normalized < 0)
            normalized = 360 - abs(normalized);

        switch (normalized) {
            // clang-format off
            case   0: move(pos, 'N', steps); return;
            case  90: move(pos, 'E', steps); return;
            case 180: move(pos, 'S', steps); return;
            case 270: move(pos, 'W', steps); return;
            // clang-format on
            default:
                throw runtime_error("invalid data");
        }
    }

    void turn(ship& ship, const char direction, const int degrees) {
        switch (direction) {
            // clang-format off
            case 'R': ship.direction += degrees; return;
            case 'L': ship.direction -= degrees; return;
            // clang-format on
            default:
                throw runtime_error("invalid data");
        }
    }

    TEST_CASE("Day 12 - Part 1 from https://adventofcode.com/2020/day/12") {
        auto instructions = loadInstructions();

        ship ship{{0, 0}, 90};
        for (const auto& instruction : instructions)
            switch (instruction.first) {
                // clang-format off
                case 'N': case 'E': case 'S': case 'W':
                    move(ship, instruction.first, instruction.second);
                    continue;
                case 'L': case 'R':
                    turn(ship, instruction.first, instruction.second);
                    continue;
                case 'F':
                    move(ship, ship.direction, instruction.second);
                    continue;
                // clang-format on
                default:
                    throw runtime_error("invalid data");
            }

        auto result = ship.getManhattenDistance();

        REQUIRE(result == 2458);
    }

    void rotate(position& pos, const char direction, const uint degrees) {
        auto absolute = direction == 'R' ? degrees : 360 - degrees;
        switch (absolute) {
            // clang-format off
            case  90: util::negate(pos.x); swap(pos.x, pos.y); return;
            case 180: util::negate(pos.x); util::negate(pos.y); return;
            case 270: util::negate(pos.y); swap(pos.x, pos.y); return;
            // clang-format on
            default:
                throw runtime_error("invalid data");
        }
    }

    TEST_CASE("Day 12 - Part 2 from https://adventofcode.com/2020/day/12#part2") {
        auto instructions = loadInstructions();

        position ship{0, 0};
        position waypoint{10, 1};
        for (const auto& instruction : instructions)
            switch (instruction.first) {
                // clang-format off
                case 'N': case 'E': case 'S': case 'W':
                    move(waypoint, instruction.first, instruction.second);
                    continue;
                case 'L': case 'R':
                    rotate(waypoint, instruction.first, instruction.second);
                    continue;
                case 'F':
                    move(ship, 'E', waypoint.x * instruction.second);
                    move(ship, 'N', waypoint.y * instruction.second);
                    continue;
                // clang-format on
                default:
                    throw runtime_error("invalid data");
            }

        auto result = ship.getManhattenDistance();

        REQUIRE(result == 145117);
    }
}
