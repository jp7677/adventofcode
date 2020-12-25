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
         int x;
         int y;
         int direction;
      };

      position pos;
      pos.x = 0;
      pos.y = 0;
      pos.direction = 90;

      for(const auto& instruction : instructions)
         switch (instruction.first) {
            case 'N':
               pos.y -= instruction.second;
               break;
            case 'E':
               pos.x += instruction.second;
               break;
            case 'S':
               pos.y += instruction.second;
               break;
            case 'W':
               pos.x -= instruction.second;
               break;
            case 'L':
               pos.direction -= instruction.second;
                break;
            case 'R':
               pos.direction += instruction.second;
                break;
            case 'F':
               auto normalized = pos.direction % 360;
               if (normalized < 0)
                  normalized = 360 - abs(normalized);

               switch (normalized) {
                  case 0:
                     pos.y -= instruction.second;
                     break;
                  case 90:
                     pos.x += instruction.second;
                     break;
                  case 180:
                     pos.y += instruction.second;
                     break;
                  case 270:
                     pos.x -= instruction.second;
                     break;
               }
               break;
         }

      auto result = abs(pos.x) + abs(pos.y);
      REQUIRE(result == 2458);
   }
}
