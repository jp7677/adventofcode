#include "days_private.h"

using namespace std;

namespace day19 {
    void loadRulesAndMessages(vector<string>& rules, vector<string>& messages) {
        auto rulesAndMessagesData = util::loadInputFile("day19-input.txt");

        auto rulesSection = true;
        for (const auto &line : rulesAndMessagesData) {
            if (line.empty())
                rulesSection = false;

            if (rulesSection) {
                auto rule = util::split(line, ':');
                auto index = stoul(rule[0]);
                if (rules.size() <= index)
                    rules.resize(index + 1);

                rules[index] = rule[1];
            } else
                messages.emplace_back(line);
        }
    }

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

                    return result + buildPattern(rules, stoul(match));
                }) +
            ")";
    }

    TEST_CASE("Day 19 - Part 1 from https://adventofcode.com/2020/day/19") {
        vector<string> rules;
        vector<string> messages;
        loadRulesAndMessages(rules, messages);

        const regex re("^" + buildPattern(rules, 0) + "$");
        auto result = count_if(messages.begin(), messages.end(),
            [&re](auto const& rule) {
                return regex_match(rule, re);
            });

        REQUIRE(result == 113);
    }

    TEST_CASE("Day 19 - Part 2 from https://adventofcode.com/2020/day/19#part2") {
        vector<string> rules;
        vector<string> messages;
        loadRulesAndMessages(rules, messages);

        auto pattern42 = buildPattern(rules, 42);
        auto pattern31 = buildPattern(rules, 31);

        auto result = 0U;
        for (auto i = 1U; i <= 4; i++) {
            auto pattern = "^(" + pattern42 + ")+";
            pattern += "(" + pattern42 + "){" + to_string(i) + "}";
            pattern += "(" + pattern31 + "){" + to_string(i) + "}$";
            const regex re(pattern);
            result += count_if(messages.begin(), messages.end(),
                [&re](auto const& rule) {
                    return regex_match(rule, re);
                });
        }

        REQUIRE(result == 253);
    }
}
