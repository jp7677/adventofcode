#include "days_private.h"

using namespace std;

namespace day14 {
   pair<bitset<36>, bitset<36>> toMaskPair(const string& maskLine) {
      return make_pair(
         bitset<36>(util::replaceAll(maskLine, 'X', '1')), 
         bitset<36>(util::replaceAll(maskLine, 'X', '0')));
   }

   uint toAddress(const string& programLine) {
      return stoi(programLine.substr(4, programLine.find(']') - 4));
   }

   ulong toValue(const string& programLine) {
      return stoul(programLine.substr(programLine.find('=') + 2));
   }

   TEST_CASE("Day 14 - Part 1 from https://adventofcode.com/2020/day/14") {
      auto programData = util::loadInputFile("day14-input.txt");

      pair<bitset<36>, bitset<36>> mask;
      unordered_map<uint, ulong> memory;
      for(const auto& programLine : programData)
         if (programLine.substr(0, 4) == "mask")
            mask = toMaskPair(programLine.substr(7));
         else {
            bitset<36> valueMask(toValue(programLine));
            valueMask &= mask.first;
            valueMask |= mask.second;

            memory.insert_or_assign(toAddress(programLine), valueMask.to_ulong());
         }

      auto result = accumulate(memory.begin(), memory.end(), 0UL,
         [](const auto sum, const auto& pair) {
            return sum + pair.second;
         });

      REQUIRE(result == 13476250121721);
   }

   TEST_CASE("Day 14 - Part 2 from https://adventofcode.com/2020/day/14#part2") {
      auto programData = util::loadInputFile("day14-input.txt");

      vector<uint> floating;
      pair<bitset<36>, bitset<36>> mask;
      unordered_map<ulong, ulong> memory;
      for(const auto& programLine : programData)
         if (programLine.substr(0, 4) == "mask") {
            auto maskLine = programLine.substr(7);
            mask = toMaskPair(maskLine);

            floating.clear();
            for (auto i = 0U; i < maskLine.size(); i++)
               if (maskLine.at(i) == 'X')
                  floating.push_back(i);
         }
         else {
            auto value = toValue(programLine);
            bitset<36> address(toAddress(programLine));
            address |= mask.second;
            address &= ~(mask.first ^ mask.second);

            auto count = 0U;
            for (auto i = 0U; i < floating.size(); i++)
               count = (count << 1) + 1;

            for (auto i = 0U; i <= count; i++) {
               bitset<36> countBits(i);
               bitset<36> actualAddress;
               for (auto y = 0U; y < floating.size(); y++)
                  if (countBits.test(y))
                     actualAddress.set(35 - floating.at(y));

               actualAddress |= address;
               memory.insert_or_assign(actualAddress.to_ulong(), value);
            }
         }

      auto result = accumulate(memory.begin(), memory.end(), 0UL,
         [](const auto sum, const auto& pair) {
            return sum + pair.second;
         });

      REQUIRE(result == 4463708436768);
   }
}
