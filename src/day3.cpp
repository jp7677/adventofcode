#include <iostream>
#include <fstream>
#include <algorithm>
#include "../inc/catch_amalgamated.hpp"

using namespace std;

vector<string> loadGridFromFile() {
   vector<string> grid;
   fstream inputStream;
   
   inputStream.open("day3-input.txt",ios::in);
   if (!inputStream.is_open())
      throw("file is not open");

   string line;
   while(getline(inputStream, line))
      grid.push_back(line);

   inputStream.close();
   return grid;
}

ulong runSlope(vector<string>* grid, int right, int down) {
   auto width = grid->at(0).size();
   auto heigth = grid->size();
   
   auto hits = 0U;
   auto posX = 0U;
   auto posY = 0U;

   while (posY < heigth) {
      if (posX >= width)
         posX = posX - width;

      if (grid->at(posY).at(posX) == '#')
         hits++;

      posY = posY + down;
      posX = posX + right;
   }
   
   return hits;
}

int day3Part1() {
   cout << "Day 3 - Part 1 from https://adventofcode.com/2020/day/3" << endl;

   auto grid = loadGridFromFile();
   return runSlope(&grid, 3, 1);
}

TEST_CASE("Day 3 - Part 1") {
   REQUIRE( day3Part1() == 265);
}

ulong day3Part2() {
   cout << "Day 3 - Part 2 from https://adventofcode.com/2020/day/3" << endl;

   auto grid = loadGridFromFile();
   return runSlope(&grid, 1, 1) *
      runSlope(&grid, 3, 1) *
      runSlope(&grid, 5, 1) *
      runSlope(&grid, 7, 1) *
      runSlope(&grid, 1, 2);
}

TEST_CASE("Day 3 - Part 2") {
   REQUIRE( day3Part2() == 3154761400);
}
