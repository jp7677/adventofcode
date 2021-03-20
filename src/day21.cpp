#include "days_private.h"

using namespace std;

namespace day21 {
    tuple<vector<string>, vector<string>> getFoodData(const string& foodDataLine) {
        auto foodData = util::split(foodDataLine, '(');
        auto ingredientsData = vector<string>(util::split(foodData[0], ' '));
        auto allergensData = util::split(foodData[1].substr(8), ',');
        transform(allergensData.begin(), allergensData.end(), allergensData.begin(),
            [](const auto& allergen) {
                return (allergen[allergen.size() - 1] == ')')
                    ? util::trim(allergen.substr(0, allergen.size() - 1))
                    : util::trim(allergen);
            });

        return {ingredientsData, allergensData};
    }

    TEST_CASE("Day 21 - Part 1 from https://adventofcode.com/2020/day/21") {
        auto foodsData = util::loadInputFile("day21-input.txt");

        vector<unordered_set<string>> foods;
        unordered_multimap<string, unordered_set<string>> foodsByAllergen;
        for (const auto& foodDataLine : foodsData) {
            auto foodData = getFoodData(foodDataLine);
            auto ingredientsData = get<0>(foodData);
            auto allergensData = get<1>(foodData);

            foods.emplace_back(unordered_set<string>());
            for (const auto& ingredient : ingredientsData)
                foods.back().insert(ingredient);

            unordered_set<string> ingredients;
            copy(ingredientsData.begin(), ingredientsData.end(), inserter(ingredients, ingredients.begin()));
            for (const auto& allergen : allergensData)
                foodsByAllergen.emplace(allergen, ingredients);
        }

        unordered_set<string> inertIngredients;
        for (const auto& foodByAllergen : foodsByAllergen)
            for (const auto& ingredient : foodByAllergen.second)
                if (find_if(foodsByAllergen.begin(), foodsByAllergen.end(),
                    [&foodByAllergen, &ingredient](const auto& otherFoodByAllergen) {
                        return otherFoodByAllergen.first == foodByAllergen.first
                            && otherFoodByAllergen.second.find(ingredient) == otherFoodByAllergen.second.end();
                        }) == foodsByAllergen.end())
                    inertIngredients.insert(ingredient);

        auto result = accumulate(foods.begin(), foods.end(), 0U,
            [&inertIngredients](auto sum, const auto& foodByIngredient) {
                return sum + count_if(foodByIngredient.begin() , foodByIngredient.end(),
                    [&inertIngredients](const auto& ingredient) {
                        return inertIngredients.find(ingredient) == inertIngredients.end();
                    });
            });

        REQUIRE(result == 2389);
    }

    TEST_CASE("Day 21 - Part 2 from https://adventofcode.com/2020/day/21#part2") {
        auto foodsData = util::loadInputFile("day21-input.txt");

        unordered_multimap<string, unordered_set<string>> foodsByAllergen;
        for (const auto& foodDataLine : foodsData) {
            auto foodData = getFoodData(foodDataLine);
            auto ingredientsData = get<0>(foodData);
            auto allergensData = get<1>(foodData);

            unordered_set<string> ingredients;
            copy(ingredientsData.begin(), ingredientsData.end(), inserter(ingredients, ingredients.begin()));
            for (const auto& allergen : allergensData)
                foodsByAllergen.emplace(allergen, ingredients);
        }

        unordered_map<string, unordered_set<string>> ingredientsByAllergen;
        for (const auto& foodByAllergen : foodsByAllergen) {
            unordered_set<string> ingredients;
            for (const auto& ingredient : foodByAllergen.second)
                if (find_if(foodsByAllergen.begin(), foodsByAllergen.end(),
                    [&foodByAllergen, &ingredient](const auto& otherFoodByAllergen) {
                        return otherFoodByAllergen.first == foodByAllergen.first
                            && otherFoodByAllergen.second.find(ingredient) == otherFoodByAllergen.second.end();
                        }) == foodsByAllergen.end())
                    ingredients.insert(ingredient);

            ingredientsByAllergen.emplace(foodByAllergen.first, ingredients);
        }

        map<string, string> ingredientsWithAllergen;
        while (!ingredientsByAllergen.empty()) {
            auto it = find_if(ingredientsByAllergen.begin(), ingredientsByAllergen.end(),
                [](const auto& ingredientByAllergen) {
                    return ingredientByAllergen.second.size() == 1;
                });

            auto ingredientWithAllergen = *it;
            auto ingredient = *ingredientWithAllergen.second.begin();
            ingredientsWithAllergen.emplace(ingredientWithAllergen.first, ingredient);
            ingredientsByAllergen.erase(it);
            for (auto& ingredientByAllergen : ingredientsByAllergen)
                ingredientByAllergen.second.erase(ingredient);
        }

        auto result = accumulate(ingredientsWithAllergen.begin(), ingredientsWithAllergen.end(), string(),
            [](auto result, const auto& ingredientWithAllergen) {
                return result + ingredientWithAllergen.second + ",";
            });
        result = util::trim(result, ",");

        REQUIRE(result == "fsr,skrxt,lqbcg,mgbv,dvjrrkv,ndnlm,xcljh,zbhp");
    }
}
