#include <iostream>
#include <fstream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"

using namespace std;

vector<vector<char>> loadGridFromFile() {
   vector<vector<char>> grid;
   fstream inputStream;
   
   inputStream.open("day3-input.txt",ios::in);
   if (!inputStream.is_open())
      throw("file is not open");

   string line;
   while(getline(inputStream, line)) {
      vector<char> gridLine;
      for (auto const& character : line)
         gridLine.push_back(character);

      grid.push_back(gridLine);
   }

   inputStream.close();
   return grid;
}

int day3Part1() {
   cout << "Day 3 - Part 1 from https://adventofcode.com/2020/day/3" << endl;

   auto grid = loadGridFromFile();
   auto width = grid.at(0).size();
   auto heigth = grid.size();
   
   auto hits = 0U;
   auto posX = 0U;
   auto posY = 0U;

   while (posY < heigth) {
      if (posX >= width)
         posX = posX - width;

      if (grid.at(posY).at(posX) == '#')
         hits++;

      posY++;
      posX = posX + 3;
   }
   
   return hits;
}

TEST_CASE("Day 3 - Part 1") {
   REQUIRE( day3Part1() == 265);
}

int day3Part2() {
   return 2;
}

TEST_CASE("Day 3 - Part 2") {
   REQUIRE( day3Part2() == 2);
}
