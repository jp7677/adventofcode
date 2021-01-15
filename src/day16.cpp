#include "days_private.h"

using namespace std;

namespace day16 {
    TEST_CASE("Day 16 - Part 1 from https://adventofcode.com/2020/day/16") {
        auto notesData = util::loadInputFile("day16-input.txt");

        unordered_map<string, vector<pair<uint, uint>>> fieldValues;
        vector<uint> yourTicket;
        vector<vector<uint>> nearbyTickets;

        auto notesSection = 0U;
        for (const auto& notesLine : notesData) {
            switch (notesSection) {
                case 0: {
                    if (notesLine.empty()) {
                        notesSection++;
                        continue;
                    }

                    auto lineElements = util::split(notesLine, ':');
                    auto valueElements = util::split(lineElements[1], ' ', '-');
                    fieldValues.emplace(lineElements[0], vector<pair<uint, uint>> {
                        make_pair(stoi(valueElements[0]), stoi(valueElements[1])),
                        make_pair(stoi(valueElements[3]), stoi(valueElements[4]))
                    });
                    break;
                }
                case 1: {
                    if (notesLine.empty()) {
                        notesSection++;
                        continue;
                    }

                    if (notesLine == "your ticket:")
                        continue;

                    auto elements = util::split(notesLine, ',');
                    transform(elements.begin(), elements.end(), back_inserter(yourTicket),
                        [](const auto& element) {
                            return stoi(element);
                        });
                    break;
                }
                case 2: {
                    if (notesLine == "nearby tickets:")
                        continue;

                    auto elements = util::split(notesLine, ',');
                    vector<uint> nearbyTicket;
                    transform(elements.begin(), elements.end(), back_inserter(nearbyTicket),
                        [](const auto& element) {
                            return stoi(element);
                        });
                    nearbyTickets.push_back(nearbyTicket);
                    break;
                }
                default: throw runtime_error("invalid data");
            }
        }

        auto result = accumulate(nearbyTickets.begin(), nearbyTickets.end(), 0U,
            [&fieldValues](const auto sum, const auto& nearbyTicket) {
                for (const auto& nearbyTicketValue : nearbyTicket) {
                    for (const auto &fieldValue : fieldValues)
                        for (const auto &value : fieldValue.second)
                            if (nearbyTicketValue >= value.first && nearbyTicketValue <= value.second)
                                goto next;

                    return sum + nearbyTicketValue;
                    next:;
                }

                return sum;
            });

        REQUIRE(result == 28884);
    }
}
