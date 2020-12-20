#include "days_private.h"

using namespace std;

namespace day7 {
   unordered_map<string, vector<pair<uint, string>>> loadLuagageRules() {
      auto rulesInput = util::loadInputFile("day7-input.txt");

      unordered_map<string, vector<pair<uint, string>>> rules;
      transform(rulesInput.begin(), rulesInput.end(), inserter(rules, rules.end()),
         [](const auto& ruleLine) {
            auto elements = util::split(ruleLine, ' ');
            auto key = elements.at(0) + "-" + elements.at(1);

            static const string containKeyword = " contain ";
            auto contains = ruleLine.substr(ruleLine.find(containKeyword) + containKeyword.size());
            auto splitted = util::split(contains, ',');

            vector<pair<uint, string>> luagages;
            transform(splitted.begin(), splitted.end(), back_inserter(luagages),
               [](const auto& value){
                  auto splitted = util::split(value, ' ');

                  return make_pair(
                     splitted.at(0) == "no" ? 0 : stoi(splitted.at(0)),
                     splitted.at(splitted.size() - 3) + "-" + splitted.at(splitted.size() - 2));
               });

            return make_pair(key, luagages);
      });
      
      return rules;
   }

   bool containsShinyGoldBag(const unordered_map<string, vector<pair<uint, string>>>* rules, const string bag) {
      auto it = rules->find(bag);
      if (it == rules->end())
         return false;

      for (const auto& luagage : it->second)
         if (luagage.second == "shiny-gold")
            return true;
         else
            if (containsShinyGoldBag(rules, luagage.second))
               return true;

      return false;
   }

   TEST_CASE("Day 7 - Part 1 from https://adventofcode.com/2020/day/7") {
      auto rules = loadLuagageRules();

      auto result = count_if(rules.begin(), rules.end(),
         [rules](const auto& rule) {
            return containsShinyGoldBag(&rules, rule.first);
         });

      REQUIRE(result == 268);
   }

   uint countBags(const unordered_map<string, vector<pair<uint, string>>>* rules, const string bag) {
      auto it = rules->find(bag);
      if (it == rules->end())
         return 0;

      return accumulate(it->second.begin(), it->second.end(), 0U,
         [rules](const auto sum, const auto& luagage) {
            return sum + luagage.first + (luagage.first * countBags(rules, luagage.second));
         });
   }

   TEST_CASE("Day 7 - Part 2 from https://adventofcode.com/2020/day/7#part2") {
      auto rules = loadLuagageRules();

      auto result = countBags(&rules, "shiny-gold");

      REQUIRE(result == 7867);
   }
}
