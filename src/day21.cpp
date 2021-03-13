#include "days_private.h"

using namespace std;

namespace day21 {
    TEST_CASE("Day 21 - Part 1 from https://adventofcode.com/2020/day/21") {
        auto foodsData = util::loadInputFile("day21-input.txt");

        vector<unordered_map<string, vector<string>>> foodsByIngredient;
        unordered_multimap<string, vector<string>> foodsByAllergen;
        for (const auto& foodDataLine : foodsData) {
            auto foodData = util::split(foodDataLine, '(');
            auto ingredientsData = util::split(foodData[0], ' ');
            auto allergensData = util::split(foodData[1].substr(8), ',');
            transform(allergensData.begin(), allergensData.end(), allergensData.begin(),
                [](const auto& allergen) {
                    return (allergen[allergen.size() - 1] == ')')
                        ? util::trim(allergen.substr(0, allergen.size() - 1))
                        : util::trim(allergen);
                });

            foodsByIngredient.emplace_back(unordered_map<string, vector<string>>());
            for (const auto& ingredient : ingredientsData)
                for (const auto &allergen : allergensData)
                    foodsByIngredient.back()[ingredient].emplace_back(allergen);

            for (const auto& allergen : allergensData)
                foodsByAllergen.emplace(allergen, ingredientsData);
        }

        set<string> ingredientsWithAllergen;
        for (const auto& foodByAllergen : foodsByAllergen)
            for (const auto& ingredient : foodByAllergen.second)
                if (find_if(foodsByAllergen.begin(), foodsByAllergen.end(),
                    [&foodByAllergen, &ingredient](const auto& otherFoodByAllergen) {
                        return otherFoodByAllergen.first == foodByAllergen.first
                            && find(otherFoodByAllergen.second.begin(), otherFoodByAllergen.second.end(), ingredient) == otherFoodByAllergen.second.end();
                    }) == foodsByAllergen.end())
                    ingredientsWithAllergen.insert(ingredient);

        auto result = accumulate(foodsByIngredient.begin(), foodsByIngredient.end(), 0U,
            [&ingredientsWithAllergen](auto sum, const auto& foodByIngredient) {
                return sum + count_if(foodByIngredient.begin() , foodByIngredient.end(),
                    [&ingredientsWithAllergen](const auto& ingredient) {
                        return ingredientsWithAllergen.find(ingredient.first) == ingredientsWithAllergen.end();
                    });
            });

        REQUIRE(result == 2389);
    }
}
