#include <iostream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

int day4Part1() {
   cout << "Day 4 - Part 1 from https://adventofcode.com/2020/day/4" << endl;

   auto passportData = loadInputFile("day4-input.txt");
   
   vector<string> passports(1);
   for(auto const& line : passportData) {
      if (line != string())
         passports.back() += line + " ";
      else
         passports.push_back(line);
   }

   return count_if(passports.begin(), passports.end(),
      [](string passport) {
         return passport.find("byr:") != string::npos && 
            passport.find("iyr:") != string::npos && 
            passport.find("eyr:") != string::npos && 
            passport.find("hgt:") != string::npos && 
            passport.find("hcl:") != string::npos && 
            passport.find("ecl:") != string::npos && 
            passport.find("pid:") != string::npos;
      });
}

TEST_CASE("Day 4 - Part 1") {
   REQUIRE( day4Part1() == 233);
}

int day4Part2() {
   cout << "Day 4 - Part 2 from https://adventofcode.com/2020/day/4" << endl;
   return 2;
}

TEST_CASE("Day 4 - Part 2") {
   REQUIRE( day4Part2() == 2);
}
