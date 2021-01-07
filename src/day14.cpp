#include "days_private.h"

using namespace std;

namespace day14 {
   TEST_CASE("Day 14 - Part 1 from https://adventofcode.com/2020/day/14") {
      auto programData = util::loadInputFile("day14-input.txt");

      unordered_map<uint, ulong> memory;
      bitset<36> zeroMask;
      bitset<36> oneMask;
      for(const auto& programLine : programData) {
         if (programLine.substr(0, 4) == "mask") {
            string zeroMaskLine(programLine.substr(7));
            
            replace(zeroMaskLine.begin(), zeroMaskLine.end(), 'X', '1');
            zeroMask = bitset<36>(zeroMaskLine);

            string oneMaskLine(programLine.substr(7));
            replace(oneMaskLine.begin(), oneMaskLine.end(), 'X', '0');
            oneMask = bitset<36>(oneMaskLine);
         }
         else {
            auto address = stoi(programLine.substr(4, programLine.find(']') - 4));
            auto value = stoul(programLine.substr(programLine.find('=') + 2));

            bitset<36> valueMask(value);
            valueMask &= zeroMask;
            valueMask |= oneMask;

            memory.insert_or_assign(address, valueMask.to_ulong());
         }
      }

      auto result = accumulate(memory.begin(), memory.end(), 0UL,
         [](const auto sum, const auto& pair) {
            return sum + pair.second;
         });

      REQUIRE(result == 13476250121721);
   }
}
