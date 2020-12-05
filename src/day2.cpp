#include <iostream>
#include <regex>
#include <fstream>
#include "../inc/catch_amalgamated.hpp"

using namespace std;

vector<string> loadEntriesFromFile() {
   vector<string> entries;
   fstream inputStream;
   
   inputStream.open("day2-input.txt",ios::in);
   if (!inputStream.is_open())
      throw("file is not open");

   string line;
   while(getline(inputStream, line))
      entries.push_back(line);

   inputStream.close();
   return entries;
}

int day2Part1() {
   cout << "Day 2 - Part 1 from https://adventofcode.com/2020/day/2" << endl;

   auto passwordEntries = loadEntriesFromFile();

   auto hits = 0;
   for (auto const& passwordEntry : passwordEntries) {
      smatch matches;
      auto success = regex_search(passwordEntry, matches, regex("(\\d+)\\-(\\d+)\\ (\\w)\\:\\s(\\w+)"));
      if(!success)
         throw("Invalid data found");

      auto minOccurences = stoi(matches[1].str());
      auto maxOccurences = stoi(matches[2].str());
      auto character = matches[3].str().at(0);
      auto password = matches[4].str();

      auto count = 0;
      for (auto const& passwordCharacter : password)
         if (passwordCharacter == character)
            count++;

      if (count >= minOccurences && count <= maxOccurences)
         hits++;
   }

   return hits;
}

TEST_CASE("Day 2 - Part 1") {
   REQUIRE(day2Part1() == 422);
}

int day2Part2() {
   cout << "Day 2 - Part 2 from https://adventofcode.com/2020/day/2" << endl;

   auto passwordEntries = loadEntriesFromFile();

   auto hits = 0;
   for (auto const& passwordEntry : passwordEntries) {
      smatch matches;
      auto success = regex_search(passwordEntry, matches, regex("(\\d+)\\-(\\d+)\\ (\\w)\\:\\s(\\w+)"));
      if(!success)
         throw("Invalid data found");

      auto first = stoi(matches[1].str());
      auto second = stoi(matches[2].str());
      auto character = matches[3].str().at(0);
      auto password = matches[4].str();

      auto match = 0;
      if (password.at(first - 1) == character)
         match++; 

      if (password.at(second - 1) == character)
         match++;

      if (match == 1)
         hits++;
   }

   return hits;
}

TEST_CASE("Day 2 - Part 2") {
   REQUIRE(day2Part2() == 451);
}