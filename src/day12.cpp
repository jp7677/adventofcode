#include "days_private.h"

using namespace std;

namespace day12 {
   struct position {
      int x = 0;
      int y = 0;
      int direction = 90;
   };

   vector<pair<char, uint>> loadInstructions() {
      auto instructionsData = util::loadInputFile("day12-input.txt");

      vector<pair<char, uint>> instructions;
      transform(instructionsData.begin(), instructionsData.end(), back_inserter(instructions),
         [](const auto& line) {
            return make_pair(line.at(0), stoi(line.substr(1)));
         });

      return instructions;
   }

   void move(position* pos, const char direction, const uint steps) {
      switch (direction) {
         case 'E': case 'W':
            pos->x += direction == 'W' ? 0 - steps : steps;
            break;
         case 'N': case 'S':
            pos->y += direction == 'S' ? 0 - steps : steps;
            break;
      }
   }

   void move(position* pos, const int direction, const uint steps) {
      auto absolute = direction % 360;
      if (absolute < 0)
         absolute = 360 - abs(absolute);

      move(pos, absolute == 90 ? 'E' : absolute == 180 ? 'S' : absolute == 270 ? 'W' : 'N', steps);
   }

   void turn(position* pos, const char direction, const uint degrees) {
      pos->direction += direction == 'L' ? 0 - degrees : degrees;
   }

   TEST_CASE("Day 12 - Part 1 from https://adventofcode.com/2020/day/12") {
      auto instructions = loadInstructions();

      position ship;
      for(const auto& instruction : instructions)
         switch (instruction.first) {
            case 'N': case 'E': case 'S': case 'W':
               move(&ship, instruction.first, instruction.second);
               break;
            case 'F':
               move(&ship, ship.direction, instruction.second);
               break;
            case 'L': case 'R':
               turn(&ship, instruction.first, instruction.second);
               break;
         }

      auto result = abs(ship.x) + abs(ship.y);

      REQUIRE(result == 2458);
   }

   void rotate(position* pos, const char direction, const uint degrees) {
      auto absolute = direction == 'R' ? degrees : 360 - degrees;
      switch (absolute) {
         case 90:
            swap(pos->x, pos->y);
            pos->y = pos->y * -1;
            break;
         case 180:
            pos->x = pos->x * -1;
            pos->y = pos->y * -1;
            break;
         case 270:
            swap(pos->x, pos->y);
            pos->x = pos->x * -1;
            break;
      }
   }

   TEST_CASE("Day 12 - Part 2 from https://adventofcode.com/2020/day/12#part2") {
      auto instructions = loadInstructions();

      position ship;
      position waypoint;
      waypoint.x = 10;
      waypoint.y = 1;
      for(const auto& instruction : instructions)
         switch (instruction.first) {
            case 'N': case 'E': case 'S': case 'W':
               move(&waypoint, instruction.first, instruction.second);
               break;
            case 'L': case 'R':
               rotate(&waypoint, instruction.first, instruction.second);
               break;
            case 'F':
               move(&ship, 'E', waypoint.x * instruction.second);
               move(&ship, 'N', waypoint.y * instruction.second);
               break;
         }

      auto result = abs(ship.x) + abs(ship.y);

      REQUIRE(result == 145117);
   }
}
