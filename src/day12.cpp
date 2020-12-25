#include "days_private.h"

using namespace std;

namespace day12 {
   struct position {
      int x = 0;
      int y = 0;
      int direction = 90;
   };

   void move(position* pos, char direction, uint steps) {
      switch (direction) {
         case 'W': case 'E':
            pos->x += direction == 'W' ? 0 - steps : steps;
            break;
         case 'N': case 'S':
            pos->y += direction == 'N' ? 0 - steps : steps;
            break;
      }
   }

   void move(position* pos, int direction, uint steps) {
      auto normalized = direction % 360;
      if (normalized < 0)
         normalized = 360 - abs(normalized);

      move(pos, normalized == 90 ? 'E' : normalized == 180 ? 'S' : normalized == 270 ? 'W' : 'N', steps);
   }

   void turn(position* pos, char direction, uint degrees) {
      pos->direction += direction == 'L' ? 0 - degrees : degrees;
   }

   TEST_CASE("Day 12 - Part 1 from https://adventofcode.com/2020/day/12") {
      auto instructionsData = util::loadInputFile("day12-input.txt");

      vector<pair<char, uint>> instructions;
      transform(instructionsData.begin(), instructionsData.end(), back_inserter(instructions),
         [](const auto& line) {
            return make_pair(line.at(0), stoi(line.substr(1)));
         });

      position pos;
      for(const auto& instruction : instructions)
         switch (instruction.first) {
            case 'N': case 'E': case 'S': case 'W':
               move(&pos, instruction.first, instruction.second);
               break;
            case 'F':
               move(&pos, pos.direction, instruction.second);
               break;
            case 'L': case 'R':
               turn(&pos, instruction.first, instruction.second);
               break;
         }

      auto result = abs(pos.x) + abs(pos.y);

      REQUIRE(result == 2458);
   }
}
