#include "days_private.h"

using namespace std;

namespace day19 {
    string buildPattern(vector<string>& rules, uint index) {
        auto rule = rules[index];
        if (rule[1] == '"')
            return string(1, rule[2]);

        auto subRules = util::split(rule, ' ');
        return "(" +
            accumulate(subRules.begin(), subRules.end(), string(),
                [&rules](const auto result, const auto match){
                    if (match == "|")
                        return result + match;

                    return result + buildPattern(rules, stoi(match));
                }) +
            ")";
    }

    TEST_CASE("Day 19 - Part 1 from https://adventofcode.com/2020/day/19") {
        auto rulesAndMessagesData = util::loadInputFile("day19-input.txt");

        auto rulesSection = true;
        vector<string> rules(rulesAndMessagesData.size());
        vector<string> messages;
        for (const auto &line : rulesAndMessagesData) {
            if (line.empty())
                rulesSection = false;

            if (rulesSection) {
                auto rule = util::split(line, ':');
                rules[stoi(rule[0])] = rule[1];
            } else
                messages.emplace_back(line);
        }

        const regex pattern("^" + buildPattern(rules, 0) + "$");
        auto result = count_if(messages.begin(), messages.end(),
            [&pattern](auto const& rule) {
                return regex_match(rule, pattern);
            });

        REQUIRE(result == 113);
    }
}
