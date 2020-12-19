#include <iostream>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day0 {
   uint day0Part1() {
      cout << "Day 0 - Part 1 from https://github.com/jp7677/adventofcode" << endl;
      return 1;
   }

   TEST_CASE("Day 0 - Part 1") {
      REQUIRE(day0Part1() == 1);
   }
}
