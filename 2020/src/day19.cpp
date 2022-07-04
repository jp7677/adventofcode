#include "days_private.h"

using namespace std;

namespace day19 {
    void loadRulesAndMessages(vector<string>& rules, vector<string>& messages) {
        auto rulesAndMessagesData = util::loadInputFile("day19-input.txt");

        auto rulesSection = true;
        for (const auto& line : rulesAndMessagesData) {
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

    string buildPattern(const vector<string>& rules, const uint index) {
        auto rule = rules[index];
        if (rule[1] == '"')
            return string(1, rule[2]); // NOLINT(modernize-return-braced-init-list)

        auto subRules = util::split(rule, ' ');
        return "("
            + accumulate(subRules.begin(), subRules.end(), string(),
                [&rules](const auto& result, const auto& subRule) {
                    if (subRule == "|")
                        return result + subRule;

                    return result + buildPattern(rules, stoul(subRule));
                })
            + ")";
    }

    TEST_CASE("Day 19 - Part 1 from https://adventofcode.com/2020/day/19") {
        vector<string> rules;
        vector<string> messages;
        loadRulesAndMessages(rules, messages);

        auto re = regex("^" + buildPattern(rules, 0) + "$");
        auto result = count_if(messages.begin(), messages.end(),
            [&re](const auto& message) {
                return regex_match(message, re);
            });

        REQUIRE(result == 113);
    }

    TEST_CASE("Day 19 - Part 2 from https://adventofcode.com/2020/day/19#part2") {
        vector<string> rules;
        vector<string> messages;
        loadRulesAndMessages(rules, messages);

        auto pattern42 = buildPattern(rules, 42);
        auto pattern31 = buildPattern(rules, 31);
        auto pattern = stringstream("^");
        for (const auto& i : array<string, 4>{"1", "2", "3", "4"})
            pattern << "((" << pattern42 << ")+(" << pattern42 << "){" << i << "}(" << pattern31 << "){" << i << "})|";

        pattern.seekp(-1, stringstream::cur);
        pattern << "$";

        auto re = regex(pattern.str());
        auto result = count_if(messages.begin(), messages.end(),
            [&re](const auto& message) {
                return regex_match(message, re);
            });

        REQUIRE(result == 253);
    }
}
