#include "days_private.h"

using namespace std;

namespace day21 {
    void loadFoods(vector<unordered_map<string, set<string>>>& foodsByIngredient, unordered_multimap<string, set<string>>& foodsByAllergen) {
        auto foodsData = util::loadInputFile("day21-input.txt");
        for (const auto& foodDataLine : foodsData) {
            auto foodData = util::split(foodDataLine, '(');
            auto ingredientsData = vector<string>(util::split(foodData[0], ' '));
            auto allergensData = util::split(foodData[1].substr(8), ',');
            transform(allergensData.begin(), allergensData.end(), allergensData.begin(),
                [](const auto& allergen) {
                    return (allergen[allergen.size() - 1] == ')')
                        ? util::trim(allergen.substr(0, allergen.size() - 1))
                        : util::trim(allergen);
                });

            foodsByIngredient.emplace_back(unordered_map<string, set<string>>());
            for (const auto& ingredient : ingredientsData)
                for (const auto &allergen : allergensData)
                    foodsByIngredient.back()[ingredient].insert(allergen);

            set<string> ingredients;
            copy(ingredientsData.begin(), ingredientsData.end(), inserter(ingredients, ingredients.begin()));
            for (const auto& allergen : allergensData)
                foodsByAllergen.emplace(allergen, ingredients);
        }
    }

    TEST_CASE("Day 21 - Part 1 from https://adventofcode.com/2020/day/21") {
        vector<unordered_map<string, set<string>>> foodsByIngredient;
        unordered_multimap<string, set<string>> foodsByAllergen;
        loadFoods(foodsByIngredient, foodsByAllergen);

        set<string> inertIngredients;
        for (const auto& foodByAllergen : foodsByAllergen)
            for (const auto& ingredient : foodByAllergen.second)
                if (find_if(foodsByAllergen.begin(), foodsByAllergen.end(),
                    [&foodByAllergen, &ingredient](const auto& otherFoodByAllergen) {
                        return otherFoodByAllergen.first == foodByAllergen.first
                            && otherFoodByAllergen.second.find(ingredient) == otherFoodByAllergen.second.end();
                        }) == foodsByAllergen.end())
                    inertIngredients.insert(ingredient);

        auto result = accumulate(foodsByIngredient.begin(), foodsByIngredient.end(), 0U,
            [&inertIngredients](auto sum, const auto& foodByIngredient) {
                return sum + count_if(foodByIngredient.begin() , foodByIngredient.end(),
                    [&inertIngredients](const auto& ingredient) {
                        return inertIngredients.find(ingredient.first) == inertIngredients.end();
                    });
            });

        REQUIRE(result == 2389);
    }

    TEST_CASE("Day 21 - Part 2 from https://adventofcode.com/2020/day/21#part2") {
        vector<unordered_map<string, set<string>>> _;
        unordered_multimap<string, set<string>> foodsByAllergen;
        loadFoods(_, foodsByAllergen);

        map<string, set<string>> ingredientsByAllergen;
        for (const auto& foodByAllergen : foodsByAllergen) {
            set<string> ingredients;
            for (const auto &ingredient : foodByAllergen.second)
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
