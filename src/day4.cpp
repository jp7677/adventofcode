#include <iostream>
#include <unordered_map>
#include <algorithm>
#include <regex>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day4 {
   vector<string> loadPassports() {
      auto passportData = util::loadInputFile("day4-input.txt");
      
      vector<string> passports(1);
      for(auto const& line : passportData) {
         if (line != string())
            passports.back() += line + " ";
         else
            passports.push_back(line);
      }

      return passports;
   }

   uint day4Part1() {
      cout << "Day 4 - Part 1 from https://adventofcode.com/2020/day/4" << endl;

      auto passports = loadPassports();
      return count_if(passports.begin(), passports.end(),
         [](const auto passport) {
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
      REQUIRE(day4Part1() == 233);
   }

   bool isValidPassport(unordered_map<string,string> passport) {
      return passport.find("byr") != passport.end() &&
         passport.find("iyr") != passport.end() &&
         passport.find("eyr") != passport.end() &&
         passport.find("hgt") != passport.end() &&
         passport.find("hcl") != passport.end() &&
         passport.find("ecl") != passport.end() &&
         passport.find("pid") != passport.end();
   }

   bool isMatch(const string value, const string pattern) {
      return regex_match(value, regex(pattern));
   }

   bool isInRange(const string value, const uint min, const uint max) {
      auto digits = util::numberOfDigits(min);
      if (!isMatch(value, "^\\d{" + to_string(digits) + "}$"))
         return false;

      auto number = stoi(value);
      return number >= min && number <= max;
   }

   bool isValidHeight(const string value) {
      if (isMatch(value, "^\\d{3}cm$"))
         return isInRange(value.substr(0, 3), 150, 193);

      if (isMatch(value, "^\\d{2}in$"))
         return isInRange(value.substr(0, 2), 59, 76);

      return false;
   }

   int day4Part2() {
      cout << "Day 4 - Part 2 from https://adventofcode.com/2020/day/4" << endl;

      auto passports = loadPassports();

      vector<unordered_map<string,string>> parsedPassports;
      transform(passports.begin(), passports.end(), back_inserter(parsedPassports),
         [](const auto passportLine) {
            auto passportFields = util::split(passportLine, ' ');

            unordered_map<string,string> parsedPassportFields;
            transform(passportFields.begin(), passportFields.end(), inserter(parsedPassportFields, parsedPassportFields.end()),
               [](const auto field) {
                  auto parsedField = util::split(field, ':');
                  return make_pair<string,string> ((string)parsedField.at(0), (string)parsedField.at(1));
               });
            return parsedPassportFields;
         });

      return count_if(parsedPassports.begin(), parsedPassports.end(),
         [](const auto passport) {
            return isValidPassport(passport) &&
               isInRange(passport.find("byr")->second, 1920, 2002) &&
               isInRange(passport.find("iyr")->second, 2010, 2020) &&
               isInRange(passport.find("eyr")->second, 2020, 2030) &&
               isValidHeight(passport.find("hgt")->second) &&
               isMatch(passport.find("hcl")->second, "^#[0-9a-f]{6}$") &&
               isMatch(passport.find("ecl")->second, "^(amb|blu|brn|gry|grn|hzl|oth)$") &&
               isMatch(passport.find("pid")->second, "^\\d{9}$");
         });
   }

   TEST_CASE("Day 4 - Part 2") {
      REQUIRE(day4Part2() == 111);
   }
}
