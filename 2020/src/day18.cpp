#include "days_private.h"

using namespace std;

namespace day18 {
    ulong evaluateWithSamePrecedence(const string& expression) {
        auto formattedExpression = util::replaceAll(util::replaceAll(expression, "(", " ( "), ")", " ) ");

        stack<pair<ulong, char>> results;
        results.emplace(0UL, '+');
        for (const auto& token : util::split(formattedExpression, ' ')) {
            ulong number;
            switch (token[0]) {
                case '+':
                case '*':
                    results.top().second = token[0];
                    continue;
                case '(':
                    results.emplace(0UL, '+');
                    continue;
                case ')':
                    number = results.top().first;
                    results.pop();
                    break;
                default:
                    number = stoi(token);
            }

            switch (results.top().second) {
                case '+':
                    results.top().first += number;
                    break;
                case '*':
                    results.top().first *= number;
                    break;
                default:
                    throw runtime_error("Invalid data found");
            }
        }
        return results.top().first;
    }

    TEST_CASE("Day 18 - Part 1 from https://adventofcode.com/2020/day/18") {
        auto mathData = util::loadInputFile("day18-input.txt");

        auto result = accumulate(mathData.begin(), mathData.end(), 0UL,
            [](const auto sum, const auto& expression) {
                return sum + evaluateWithSamePrecedence(expression);
            });

        REQUIRE(result == 4297397455886);
    }

    ulong simpleEvaluateWithAdditionPrecedence(const string& expression) {
        static const auto re = regex(R"(\d+( \+ \d+)+)");
        auto bracedExpression = expression;
        for (auto it = sregex_iterator(expression.begin(), expression.end(), re); it != sregex_iterator(); it++) {
            auto expr = it->str();
            bracedExpression = bracedExpression.replace(bracedExpression.find(expr), expr.length(), string("(").append(expr).append(")"));
        }

        return evaluateWithSamePrecedence(bracedExpression);
    }

    ulong evaluateWithAdditionPrecedence(const string& expression) {
        static const auto re = regex(R"(\([\d\*\+\ ]+\))");
        auto steps = expression;
        smatch match;
        while (regex_search(steps, match, re)) {
            auto expr = match.str();
            steps = steps.replace(steps.find(expr), expr.length(), to_string(simpleEvaluateWithAdditionPrecedence(expr)));
        }

        return simpleEvaluateWithAdditionPrecedence(steps);
    }

    TEST_CASE("Day 18 - Part 2 from https://adventofcode.com/2020/day/18#part2") {
        auto mathData = util::loadInputFile("day18-input.txt");

        auto result = accumulate(mathData.begin(), mathData.end(), 0UL,
            [](const auto sum, const auto& expression) {
                return sum + evaluateWithAdditionPrecedence(expression);
            });

        REQUIRE(result == 93000656194428);
    }
}
