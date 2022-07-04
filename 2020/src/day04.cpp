#include "days_private.h"

using namespace std;

namespace day04 {
    vector<unordered_map<string, string>> loadPassports() {
        auto passportsInput = util::loadInputFile("day04-input.txt");

        vector<string> passports(1);
        for (const auto& line : passportsInput) {
            if (!line.empty())
                passports.back() += line + " ";
            else
                passports.push_back(line);
        }

        vector<unordered_map<string, string>> parsedPassports;
        transform(passports.begin(), passports.end(), back_inserter(parsedPassports),
            [](const auto& passportLine) {
                auto passportFields = util::split(passportLine, ' ');

                unordered_map<string, string> parsedPassportFields;
                transform(passportFields.begin(), passportFields.end(), inserter(parsedPassportFields, parsedPassportFields.end()),
                    [](const auto& field) {
                        auto splitted = util::split(field, ':');
                        return make_pair(splitted[0], splitted[1]);
                    });
                return parsedPassportFields;
            });

        return parsedPassports;
    }

    bool isValidPassport(const unordered_map<string, string>& passport) {
        return passport.find("byr") != passport.end()
            && passport.find("iyr") != passport.end()
            && passport.find("eyr") != passport.end()
            && passport.find("hgt") != passport.end()
            && passport.find("hcl") != passport.end()
            && passport.find("ecl") != passport.end()
            && passport.find("pid") != passport.end();
    }

    TEST_CASE("Day 04 - Part 1 from https://adventofcode.com/2020/day/4") {
        auto passports = loadPassports();

        auto result = count_if(passports.begin(), passports.end(),
            [](const auto& passport) {
                return isValidPassport(passport);
            });

        REQUIRE(result == 233);
    }

    bool isMatch(const string& value, const regex& pattern) {
        return regex_match(value, pattern);
    }

    bool isInRange(const string& value, const uint min, const uint max) {
        static const auto digits2Re = regex("^\\d{2}$");
        static const auto digits3Re = regex("^\\d{3}$");
        static const auto digits4Re = regex("^\\d{4}$");

        switch (util::numberOfDigits(min)) {
            // clang-format off
            case 2: if (!isMatch(value, digits2Re)) return false; break;
            case 3: if (!isMatch(value, digits3Re)) return false; break;
            case 4: if (!isMatch(value, digits4Re)) return false; break;
            // clang-format on
            default:
                throw runtime_error("invalid data");
        }

        auto number = stoul(value);
        return number >= min && number <= max;
    }

    bool isValidHeight(const string& value) {
        static const auto cmRe = regex("^\\d{3}cm$");
        static const auto inRe = regex("^\\d{2}in$");

        if (isMatch(value, cmRe))
            return isInRange(value.substr(0, 3), 150, 193);

        if (isMatch(value, inRe))
            return isInRange(value.substr(0, 2), 59, 76);

        return false;
    }

    TEST_CASE("Day 04 - Part 2 from https://adventofcode.com/2020/day/4#part2") {
        auto passports = loadPassports();

        static const auto hclRe = regex("^#[0-9a-f]{6}$");
        static const auto eclRe = regex("^(amb|blu|brn|gry|grn|hzl|oth)$");
        static const auto pidRe = regex("^\\d{9}$");

        auto result = count_if(passports.begin(), passports.end(),
            [](const auto& passport) {
                return isValidPassport(passport)
                    && isInRange(passport.find("byr")->second, 1920, 2002)
                    && isInRange(passport.find("iyr")->second, 2010, 2020)
                    && isInRange(passport.find("eyr")->second, 2020, 2030)
                    && isValidHeight(passport.find("hgt")->second)
                    && isMatch(passport.find("hcl")->second, hclRe)
                    && isMatch(passport.find("ecl")->second, eclRe)
                    && isMatch(passport.find("pid")->second, pidRe);
            });

        REQUIRE(result == 111);
    }
}
