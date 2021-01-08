#include "days_private.h"

using namespace std;

namespace day08 {
   vector<pair<string, int>> loadProgram() {
      auto programData = util::loadInputFile("day08-input.txt");

      vector<pair<string, int>> program;
      transform(programData.begin(), programData.end(), back_inserter(program),
         [](const auto& programLine) {
            return make_pair(
               programLine.substr(0, 3),
               stoi(programLine.substr(4)));
         });

      return program;
   }

   TEST_CASE("Day 08 - Part 1 from https://adventofcode.com/2020/day/8") {
      auto program = loadProgram();

      auto result = 0;
      auto pos = 0U;
      set<uint> visited;
      while (visited.find(pos) == visited.end()) {
         visited.insert(pos);
         auto instruction = program.at(pos);
         if (instruction.first == "acc")
            result += instruction.second;
         
         pos += instruction.first == "jmp" ? instruction.second : 1;
      }

      REQUIRE(result == 1810);
   }

   TEST_CASE("Day 08 - Part 2 from https://adventofcode.com/2020/day/8#part2") {
      auto program = loadProgram();

      auto result = [&program]{
         for (const auto& fix : program) {
            if (fix.first == "acc")
               continue;

            auto acc = 0;
            auto pos = 0U;
            set<uint> visited;
            while (visited.find(pos) == visited.end()) {
               visited.insert(pos);
               auto instruction = program.at(pos);
               if (instruction.first == "acc")
                  acc += instruction.second;

               pos += instruction.first != "acc" && instruction.first == "jmp" && fix != instruction ? instruction.second : 1;
               if (pos >= program.size())
                  return acc;
            }
         }

         throw runtime_error("Invalid data");
      }();

      REQUIRE(result == 969);
   }
}
