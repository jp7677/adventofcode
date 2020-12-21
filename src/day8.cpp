#include "days_private.h"

using namespace std;

namespace day8 {
   TEST_CASE("Day 8 - Part 1 from https://adventofcode.com/2020/day/8") {
      auto programmData = util::loadInputFile("day8-input.txt");

      vector<pair<string, int>> program;
      transform(programmData.begin(), programmData.end(), back_inserter(program),
         [](const auto& programLine) {
            return make_pair(
               programLine.substr(0, 3),
               stoi(programLine.substr(4)));
      });

      auto acc = 0;
      auto pos = 0U;
      set<uint> visited;
      while (visited.find(pos) == visited.end()) {
         auto instruction = program.at(pos);
         if (instruction.first == "acc")
            acc += instruction.second;

         visited.insert(pos);
         pos += instruction.first == "jmp" ? instruction.second : 1;
      }

      REQUIRE(acc == 1810);
   }
}
