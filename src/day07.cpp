#include "days_private.h"

using namespace std;

namespace day07 {
   unordered_map<string, vector<pair<uint, string>>> loadLuggageRules() {
      auto rulesInput = util::loadInputFile("day07-input.txt");

      unordered_map<string, vector<pair<uint, string>>> rules;
      transform(rulesInput.begin(), rulesInput.end(), inserter(rules, rules.end()),
         [](const auto& ruleLine) {
            auto elements = util::split(ruleLine, ' ');
            auto key = elements.at(0) + "-" + elements.at(1);

            static const auto containKeyword = string(" contain ");
            auto contains = ruleLine.substr(ruleLine.find(containKeyword) + containKeyword.size());
            auto splitted = util::split(contains, ',');

            vector<pair<uint, string>> luggages;
            transform(splitted.begin(), splitted.end(), back_inserter(luggages),
               [](const auto& value){
                  auto splitted = util::split(value, ' ');

                  return make_pair(
                     splitted.at(0) == "no" ? 0 : stoi(splitted.at(0)),
                     splitted.at(splitted.size() - 3) + "-" + splitted.at(splitted.size() - 2));
               });

            return make_pair(key, luggages);
         });
      
      return rules;
   }

   bool containsShinyGoldBag(const unordered_map<string, vector<pair<uint, string>>>& rules, const string bag) {
      auto it = rules.find(bag);
      if (it == rules.end())
         return false;

      for (const auto& luggage : it->second)
         if (luggage.second == "shiny-gold")
            return true;
         else
            if (containsShinyGoldBag(rules, luggage.second))
               return true;

      return false;
   }

   TEST_CASE("Day 07 - Part 1 from https://adventofcode.com/2020/day/7") {
      auto rules = loadLuggageRules();

      auto result = count_if(rules.begin(), rules.end(),
         [&rules](const auto& rule) {
            return containsShinyGoldBag(rules, rule.first);
         });

      REQUIRE(result == 268);
   }

   uint countBags(const unordered_map<string, vector<pair<uint, string>>>& rules, const string bag) {
      auto it = rules.find(bag);
      if (it == rules.end())
         return 0;

      return accumulate(it->second.begin(), it->second.end(), 0U,
         [&rules](const auto sum, const auto& luggage) {
            return sum + luggage.first + (luggage.first * countBags(rules, luggage.second));
         });
   }

   TEST_CASE("Day 07 - Part 2 from https://adventofcode.com/2020/day/7#part2") {
      auto rules = loadLuggageRules();

      auto result = countBags(rules, "shiny-gold");

      REQUIRE(result == 7867);
   }
}
