#include "days_private.h"

using namespace std;

namespace day12 {
   TEST_CASE("Day 12 - Part 1 from https://adventofcode.com/2020/day/12") {
      auto instructionsData = util::loadInputFile("day12-input.txt");

      vector<pair<char, uint>> instructions;
      transform(instructionsData.begin(), instructionsData.end(), back_inserter(instructions),
         [](const auto& line) {
            return make_pair(line.at(0), stoi(line.substr(1)));
         });

      struct position {
         int x = 0;
         int y = 0;
         int direction = 90;
      };

      position pos;
      for(const auto& instruction : instructions)
         switch (instruction.first) {
            case 'N':
            case 'E':
               pos.x += instruction.first == 'N' ? 0 - instruction.second : instruction.second;
               break;
            case 'S':
            case 'W':
               pos.x -= instruction.first == 'S' ? 0 - instruction.second : instruction.second;
               break;
            case 'L':
            case 'R':
               pos.direction += instruction.first == 'L' ? 0 - instruction.second : instruction.second;
                break;
            case 'F':
               auto normalized = pos.direction % 360;
               if (normalized < 0)
                  normalized = 360 - abs(normalized);

               switch (normalized) {
                  case 90:
                  case 270:
                     pos.x += normalized == 90 ? instruction.second : 0 - instruction.second;
                     break;
                  case 0:
                  case 180:
                     pos.y += normalized == 0 ? 0 - instruction.second : instruction.second;
                     break;
               }
               break;
         }

      auto result = abs(pos.x) + abs(pos.y);
      REQUIRE(result == 2458);
   }
}
