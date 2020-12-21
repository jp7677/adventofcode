#include "days_private.h"

using namespace std;

namespace day8 {
   vector<pair<string, int>> loadProgram() {
      auto programmData = util::loadInputFile("day8-input.txt");

      vector<pair<string, int>> program;
      transform(programmData.begin(), programmData.end(), back_inserter(program),
         [](const auto& programLine) {
            return make_pair(
               programLine.substr(0, 3),
               stoi(programLine.substr(4)));
      });

      return program;
   }

   TEST_CASE("Day 8 - Part 1 from https://adventofcode.com/2020/day/8") {
      auto program = loadProgram();

      auto acc = 0;
      auto pos = 0U;
      set<uint> visited;
      while (visited.find(pos) == visited.end()) {
         visited.insert(pos);
         auto instruction = program.at(pos);
         if (instruction.first == "acc")
            acc += instruction.second;
         
         pos += instruction.first == "jmp" ? instruction.second : 1;
      }

      REQUIRE(acc == 1810);
   }

   TEST_CASE("Day 8 - Part 2 from https://adventofcode.com/2020/day/8#part2") {
      auto program = loadProgram();

      auto acc = 0;
      auto pos = 0U, fix = 0U;
      set<uint> visited;
      while (pos < program.size()) {
         acc = pos = 0;
         visited.clear();
         while (visited.find(pos) == visited.end() && pos < program.size()) {
            visited.insert(pos);
            auto instruction = program.at(pos);
            if (instruction.first == "acc")
               acc += instruction.second;

            pos += instruction.first != "acc" && instruction.first == "jmp" && fix != pos ? instruction.second : 1;
         }
         fix++;
      }

      REQUIRE(acc == 969);
   }
}
