#include<iostream>
#include<vector>
#include "../inc/catch_amalgamated.hpp"

using namespace std;

int day1() {
   cout << "Day 1 from https://adventofcode.com/2020/day/1" << endl;

   vector<int> list { 1721, 979, 366, 299, 675, 1456 };

   for (auto const& element1 : list)
      for (auto const& element2 : list)
         if (element1 + element2 == 2020)
            return element1 * element2;

   return 0;
}

TEST_CASE("Day 1") {
   REQUIRE( day1() == 514579);
}
