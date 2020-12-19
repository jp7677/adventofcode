#include <iostream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day7 {
   const auto containKeyword = string(" contain ");
   const auto ownBag = string("shiny-gold");

   bool holdsOwnBag(const unordered_map<string, vector<string>>* rules, const string bag) {
      auto it = rules->find(bag);
      if (it == rules->end())
         return false;

      for (const auto& value : it->second)
         if (value == ownBag)
            return true;
         else
            if (holdsOwnBag(rules, value))
               return true;

      return false;
   }

   uint day7Part1() {
      cout << "Day 7 - Part 1 from https://adventofcode.com/2020/day/7" << endl;

      auto rulesInput = util::loadInputFile("day7-input.txt");

      unordered_map<string, vector<string>> rules;
      transform(rulesInput.begin(), rulesInput.end(), inserter(rules, rules.end()),
         [](const auto& ruleLine) {
            auto elements = util::split(ruleLine, ' ');
            auto key = elements.at(0) + "-" + elements.at(1);

            auto contains = ruleLine.substr(ruleLine.find(containKeyword) + containKeyword.size());
            auto values = util::split(contains, ',');

            transform(values.begin(), values.end(), values.begin(),
               [](const auto& value){
                  auto splitted = util::split(value, ' ');
                  return splitted.at(splitted.size() - 3) + "-" + splitted.at(splitted.size() - 2);
               });

            return make_pair(key, values);
      });

      return count_if(rules.begin(), rules.end(),
         [rules](const auto& rule) {
            return holdsOwnBag(&rules, rule.first);
         });
   }

   TEST_CASE("Day 7 - Part 1") {
      REQUIRE(day7Part1() == 268);
   }
}
