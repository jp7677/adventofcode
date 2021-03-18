#include "days_private.h"

using namespace std;

namespace day22 {
    void loadDecks(vector<uint>& player1Cards, vector<uint>& player2Cards) {
        auto decksData = util::loadInputFile("day22-input.txt");

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
    }

    uint calculateScore(vector<uint>& winningCards) {
        auto score = 0U;
        for (auto i = winningCards.size(); i > 0; i--)
            score += winningCards[winningCards.size() - i] * i;

        return score;
    }

    TEST_CASE("Day 22 - Part 1 from https://adventofcode.com/2020/day/22") {
        vector<uint> player1Cards;
        vector<uint> player2Cards;
        loadDecks(player1Cards, player2Cards);

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

        auto result = calculateScore(!player1Cards.empty() ? player1Cards : player2Cards);

        REQUIRE(result == 34664);
    }

    bool playGame(vector<uint>& player1Cards, vector<uint>& player2Cards) {
        vector<pair<vector<uint>, vector<uint>>> previousGames;

        while (!player1Cards.empty() && !player2Cards.empty()) {
            auto previouslyPlayed = false;
            for (const auto& previousGame : previousGames)
                if (equal(previousGame.first.begin(), previousGame.first.end(), player1Cards.begin())
                    && equal(previousGame.second.begin(), previousGame.second.end(), player2Cards.begin())) {
                    previouslyPlayed = true;
                    break;
                }

            previousGames.emplace_back(make_pair(player1Cards, player2Cards));

            if (previouslyPlayed)
                return true;

            auto player1Wins = false;
            if (player1Cards.size() - 1 >= player1Cards.front() && player2Cards.size() - 1 >= player2Cards.front()) {
                auto player1CardsCopy = vector<uint>(player1Cards.begin() + 1, player1Cards.begin() + player1Cards.front() + 1);
                auto player2CardsCopy = vector<uint>(player2Cards.begin() + 1, player2Cards.begin() + player2Cards.front() + 1);
                player1Wins = playGame(player1CardsCopy, player2CardsCopy);
            } else if (player1Cards.front() > player2Cards.front())
                player1Wins = true;

            if (player1Wins) {
                player1Cards.push_back(player1Cards.front());
                player1Cards.push_back(player2Cards.front());
            } else {
                player2Cards.push_back(player2Cards.front());
                player2Cards.push_back(player1Cards.front());
            }

            player1Cards.erase(player1Cards.begin(), next(player1Cards.begin()));
            player2Cards.erase(player2Cards.begin(), next(player2Cards.begin()));
        }

        return !player1Cards.empty();
    }

    TEST_CASE("Day 22 - Part 2 from https://adventofcode.com/2020/day/22#part2") {
        vector<uint> player1Cards;
        vector<uint> player2Cards;
        loadDecks(player1Cards, player2Cards);

        auto player1Wins = playGame(player1Cards, player2Cards);
        auto result = calculateScore(player1Wins ? player1Cards : player2Cards);

        REQUIRE(result == 32018);
    }
}
