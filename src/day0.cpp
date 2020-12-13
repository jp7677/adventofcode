#include <iostream>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day0 {
   uint day0Part1() {
      cout << "Day 0 - Part 1 from https://adventofcode.com/2020/day/0" << endl;
      return 1;
   }

   TEST_CASE("Day 0 - Part 1") {
      REQUIRE( day0Part1() == 1);
   }

   uint day0Part2() {
      cout << "Day 0 - Part 2 from https://adventofcode.com/2020/day/0" << endl;
      return 2;
   }

   TEST_CASE("Day 0 - Part 2") {
      REQUIRE( day0Part2() == 2);
   }
}
