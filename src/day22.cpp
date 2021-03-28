#include "days_private.h"

using namespace std;

namespace day22 {
    void loadDecks(deque<uint>& player1Cards, deque<uint>& player2Cards) {
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

    uint calculateScore(const deque<uint>& winningCards) {
        auto score = 0U;
        for (auto i = winningCards.size(); i > 0; i--)
            score += winningCards[winningCards.size() - i] * i;

        return score;
    }

    TEST_CASE("Day 22 - Part 1 from https://adventofcode.com/2020/day/22") {
        deque<uint> player1Cards;
        deque<uint> player2Cards;
        loadDecks(player1Cards, player2Cards);

        while (!player1Cards.empty() && !player2Cards.empty()) {
            if (player1Cards.front() > player2Cards.front()) {
                player1Cards.push_back(player1Cards.front());
                player1Cards.push_back(player2Cards.front());
            } else {
                player2Cards.push_back(player2Cards.front());
                player2Cards.push_back(player1Cards.front());
            }

            player1Cards.pop_front();
            player2Cards.pop_front();
        }

        auto result = calculateScore(!player1Cards.empty() ? player1Cards : player2Cards);

        REQUIRE(result == 34664);
    }

    size_t hash(const deque<uint>& cards) {
        auto game = 0U;
        for (auto i = 0U; i < cards.size(); i++)
            game ^= (cards[i] << (i * 4)); // Beware there be dragons.

        return game;
    }

    bool playGame(deque<uint>& player1Cards, deque<uint>& player2Cards) {
        unordered_set<size_t> previousGames;

        while (!player1Cards.empty() && !player2Cards.empty()) {
            auto game = hash(player1Cards);
            if (previousGames.find(game) != previousGames.end())
                return true;

            auto player1Wins = false;
            if (player1Cards.size() - 1 >= player1Cards.front() && player2Cards.size() - 1 >= player2Cards.front()) {
                auto player1CardsCopy = deque<uint>(next(player1Cards.begin()), next(player1Cards.begin() + player1Cards.front()));
                auto player2CardsCopy = deque<uint>(next(player2Cards.begin()), next(player2Cards.begin() + player2Cards.front()));
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

            player1Cards.pop_front();
            player2Cards.pop_front();
            previousGames.insert(game);
        }

        return !player1Cards.empty();
    }

    TEST_CASE("Day 22 - Part 2 from https://adventofcode.com/2020/day/22#part2") {
        deque<uint> player1Cards;
        deque<uint> player2Cards;
        loadDecks(player1Cards, player2Cards);

        auto player1Wins = playGame(player1Cards, player2Cards);
        auto result = calculateScore(player1Wins ? player1Cards : player2Cards);

        REQUIRE(result == 32018);
    }
}
