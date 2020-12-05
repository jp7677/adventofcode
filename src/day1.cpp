#include <iostream>
#include <fstream>
#include <vector>
#include "../inc/catch_amalgamated.hpp"

using namespace std;

vector<int> loadEntriesFromFile() {
   vector<int> entries;
   fstream inputStream;
   
   inputStream.open("day1-input.txt",ios::in);
   if (!inputStream.is_open())
      return entries;

   string line;
   while(getline(inputStream, line))
      entries.push_back(stoi(line));

   inputStream.close();
   return entries;
}

int day1Part1() {
   cout << "Day 1 - Part 1 from https://adventofcode.com/2020/day/1" << endl;

   auto entries = loadEntriesFromFile();
   for (auto const& element1 : entries)
      for (auto const& element2 : entries)
         if (element1 + element2 == 2020)
            return element1 * element2;

   return 0;
}

TEST_CASE("Day 1 - Part 1") {
   REQUIRE(day1Part1() == 964875);
}

int day1Part2() {
   cout << "Day 1 - Part 2 from https://adventofcode.com/2020/day/1" << endl;

   auto entries = loadEntriesFromFile();
   for (auto const& element1 : entries)
      for (auto const& element2 : entries)
         for (auto const& element3 : entries)
            if (element1 + element2 + element3 == 2020)
               return element1 * element2 * element3;

   return 0;
}

TEST_CASE("Day 1 - Part 2") {
   REQUIRE(day1Part2() == 158661360);
}
