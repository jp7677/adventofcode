#include <iostream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"
#include "util.h"

using namespace std;

namespace day7 {
   const auto containKeyword = string(" contain ");

   bool holdsOwnBag(const unordered_map<string, vector<string>>* rules, const string bag) {
      auto it = rules->find(bag);
      if (it == rules->end())
         return false;

      for (const auto& value : it->second)
         if (value == "shiny-gold")
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

   struct Luggage {
      uint count;
      string bag;
   };

    uint countBags(const unordered_map<string, vector<Luggage>>* rules, const string bag) {
      auto it = rules->find(bag);
      if (it == rules->end())
         return 0;
     
      return accumulate(it->second.begin(), it->second.end(), 0U,
         [rules](const auto sum, auto& luagage) {
            return sum + luagage.count + (luagage.count * countBags(rules, luagage.bag));
         });
   }

   uint day7Part2() {
      cout << "Day 7 - Part 2 from https://adventofcode.com/2020/day/7#part2" << endl;

      auto rulesInput = util::loadInputFile("day7-input.txt");

      unordered_map<string, vector<Luggage>> rules;
      transform(rulesInput.begin(), rulesInput.end(), inserter(rules, rules.end()),
         [](const auto& ruleLine) {
            auto elements = util::split(ruleLine, ' ');
            auto key = elements.at(0) + "-" + elements.at(1);

            auto contains = ruleLine.substr(ruleLine.find(containKeyword) + containKeyword.size());
            auto splitted = util::split(contains, ',');

            vector<Luggage> values;
            transform(splitted.begin(), splitted.end(), inserter(values, values.end()),
               [](const auto& value){
                  auto splitted = util::split(value, ' ');

                  struct Luggage luggage;
                  luggage.bag = splitted.at(splitted.size() - 3) + "-" + splitted.at(splitted.size() - 2);
                  luggage.count = splitted.at(0) == "no" ? 0 : stoi(splitted.at(0));
                  return luggage;
               });

            return make_pair(key, values);
      });

      return countBags(&rules, "shiny-gold");
   }

   TEST_CASE("Day 7 - Part 2") {
      REQUIRE(day7Part2() == 7867);
   }
}
