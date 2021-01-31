#include "days_private.h"

using namespace std;

namespace day00 {
    void surroundAllWithSpace(string& expression, const char token) {
        auto surrounded = string(" ");
        surrounded += token;
        surrounded.append(" ");
        for (auto position = 0UL; ; position += 3) {
            position = expression.find(token, position );
            if (position == string::npos)
                return;

            expression.erase(position, 1);
            expression.insert(position, surrounded );
        }
    }

    ulong evaluateWithSamePrecedence(const string& expression) {
        auto formattedExpression = expression;
        surroundAllWithSpace(formattedExpression, '(');
        surroundAllWithSpace(formattedExpression, ')');

        stack<pair<ulong, char>> results;
        results.emplace(0UL, '+');
        for (const auto& token : util::split(formattedExpression, ' ')) {
            ulong number;

            if (token == " ")
                continue;

            if (token == "+" || token == "*") {
                results.top().second = token[0];
                continue;
            }

            if (token == "(") {
                results.emplace(0UL, '+');
                continue;
            }

            if (token == ")") {
                number = results.top().first;
                results.pop();
            } else {
                number = stoi(token);
            }

            results.top().first = results.top().second == '+'
                ? results.top().first + number
                : results.top().first * number;
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
        for (auto it = sregex_iterator(expression.begin(), expression.end(), re); it != sregex_iterator(); it++)
        {
            auto expr = (*it).str();
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
