#include "days_private.h"

using namespace std;

namespace day22 {
    TEST_CASE("Day 22 - Part 1 from https://adventofcode.com/2020/day/22") {
        auto decksData = util::loadInputFile("day22-input.txt");

        vector<uint> player1Cards;
        vector<uint> player2Cards;
        bool player2Section = false;
        for (const auto& deckData : decksData) {
            if (deckData.empty())
                player2Section = true;

            if (deckData[0] == 'P' || deckData.empty())
                continue;

            if (!player2Section)
                player1Cards.push_back(stoi(deckData));
            else
                player2Cards.push_back(stoi(deckData));
        }

        while (!player1Cards.empty() && !player2Cards.empty()) {
            if (player1Cards.front() > player2Cards.front()) {
                player1Cards.push_back(player1Cards.front());
                player1Cards.push_back(player2Cards.front());
            } else {
                player2Cards.push_back(player2Cards.front());
                player2Cards.push_back(player1Cards.front());
            }

            player1Cards.erase(player1Cards.begin(), next(player1Cards.begin()));
            player2Cards.erase(player2Cards.begin(), next(player2Cards.begin()));
        }

        auto winningCards = !player1Cards.empty() ? player1Cards : player2Cards;

        auto result = 0U;
        for (auto i = winningCards.size(); i > 0; i--)
            result += winningCards[winningCards.size() - i] * i;

        REQUIRE(result == 34664);
    }
}
