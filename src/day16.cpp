#include "days_private.h"

using namespace std;

namespace day16 {
    void loadNotes(vector<pair<string, vector<pair<uint, uint>>>>& ticketFields, vector<uint>& yourTicket, vector<vector<uint>>& nearbyTickets) {
        auto notesData = util::loadInputFile("day16-input.txt");

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
                    ticketFields.emplace_back(make_pair(lineElements[0], vector<pair<uint, uint>> {
                        make_pair(stoi(valueElements[0]), stoi(valueElements[1])),
                        make_pair(stoi(valueElements[3]), stoi(valueElements[4]))
                    }));
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
    }

    bool isValidTicketValue(const uint& ticketValue, const pair<string, vector<pair<uint, uint>>>& fieldValue) {
        return (ticketValue >= fieldValue.second[0].first && ticketValue <= fieldValue.second[0].second)
            || (ticketValue >= fieldValue.second[1].first && ticketValue <= fieldValue.second[1].second);
    }

    pair<bool, uint> getInvalidTicketValue(const vector<uint>& nearbyTicket, const vector<pair<string, vector<pair<uint, uint>>>>& ticketFields) {
        for (const auto& nearbyTicketValue : nearbyTicket) {
            for (const auto &ticketField : ticketFields)
                if (isValidTicketValue(nearbyTicketValue, ticketField))
                    goto next;

            return make_pair(true, nearbyTicketValue);
            next:;
        }

        return make_pair(false, 0);
    }

    TEST_CASE("Day 16 - Part 1 from https://adventofcode.com/2020/day/16") {
        vector<pair<string, vector<pair<uint, uint>>>> ticketFields;
        vector<uint> yourTicket;
        vector<vector<uint>> nearbyTickets;

        loadNotes(ticketFields, yourTicket, nearbyTickets);

        auto result = accumulate(nearbyTickets.begin(), nearbyTickets.end(), 0U,
            [&ticketFields](const auto sum, const auto& nearbyTicket) {
                return sum + getInvalidTicketValue(nearbyTicket, ticketFields).second;
            });

        REQUIRE(result == 28884);
    }

    TEST_CASE("Day 16 - Part 2 from https://adventofcode.com/2020/day/16#part2") {
        vector<pair<string, vector<pair<uint, uint>>>> ticketFields;
        vector<uint> yourTicket;
        vector<vector<uint>> nearbyTickets;

        loadNotes(ticketFields, yourTicket, nearbyTickets);

        nearbyTickets.erase(remove_if(nearbyTickets.begin(), nearbyTickets.end(),
                [&ticketFields](const auto& nearbyTicket) {
                    return getInvalidTicketValue(nearbyTicket, ticketFields).first;
                }),
            nearbyTickets.end());

        vector<vector<string>> ticketFieldOptions(ticketFields.size());
        for (const auto& nearbyTicket : nearbyTickets)
            for (auto i = 0U; i < nearbyTicket.size(); i++)
                for (const auto& ticketField : ticketFields)
                    if (!isValidTicketValue(nearbyTicket.at(i), ticketField)
                        && find(ticketFieldOptions.at(i).begin(), ticketFieldOptions.at(i).end(), ticketField.first) == ticketFieldOptions.at(i).end())
                        ticketFieldOptions.at(i).push_back(ticketField.first);

        vector<string> fieldNames;
        transform(ticketFields.begin(), ticketFields.end(), back_inserter(fieldNames),
            [](const auto& field) {
                return field.first;
            });
        sort(fieldNames.begin(), fieldNames.end());

        for (auto& fieldOption : ticketFieldOptions) {
            vector<string> possibleFieldOptions;
            sort(fieldOption.begin(), fieldOption.end());
            set_difference(fieldNames.begin(), fieldNames.end(), fieldOption.begin(), fieldOption.end(), back_inserter(possibleFieldOptions));
            fieldOption = possibleFieldOptions;
        }

        vector<string> finalTicketPositions{ticketFieldOptions.size(), string()};
        while (find(finalTicketPositions.begin(), finalTicketPositions.end(), string()) != finalTicketPositions.end()) {
            auto findFinalIt = find_if(ticketFieldOptions.begin(), ticketFieldOptions.end(),
               [](const auto& fieldPosition){
                    return fieldPosition.size() == 1;
                });

            auto fieldName = (*findFinalIt).at(0);
            finalTicketPositions.at(distance(ticketFieldOptions.begin(), findFinalIt)) = fieldName;

            for (auto& fieldOption : ticketFieldOptions) {
                auto findNameIt = find(fieldOption.begin(), fieldOption.end(), fieldName);
                if (findNameIt != fieldOption.end())
                    fieldOption.erase(findNameIt);
            }
        }

        auto result = 1UL;
        for (auto i = 0U; i < finalTicketPositions.size(); ++i)
            if (finalTicketPositions.at(i).rfind("departure", 0) == 0)
                result *= yourTicket.at(i);

        REQUIRE(result == 1001849322119);
    }
}
