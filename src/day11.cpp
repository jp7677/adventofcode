#include "days_private.h"

using namespace std;

namespace day11 {
   string getAdjacentSeats(const vector<string>* map, const int x, const int y) {
      string seats;
      if (x > 0 && y > 0)
         seats.push_back(map->at(y - 1).at(x - 1));
      if (y > 0)
         seats.push_back(map->at(y - 1).at(x));
      if (x < map->at(0).size() - 1 && y > 0)
         seats.push_back(map->at(y - 1).at(x + 1));

      if (x > 0)
         seats.push_back(map->at(y).at(x - 1));
      if (x < map->at(0).size() - 1)
         seats.push_back(map->at(y).at(x + 1));

      if (x > 0 && y < map->size() - 1)
         seats.push_back(map->at(y + 1).at(x - 1));
      if (y < map->size() - 1)
         seats.push_back(map->at(y + 1).at(x));
      if (x < map->at(0).size() -1 && y < map->size() - 1)
         seats.push_back(map->at(y + 1).at(x + 1));
      
      return seats;
   }

   bool needsSwapDueToAdjacentSeats(vector<string>* map, const int x, const int y) {
      auto seat = map->at(y).at(x);
      if (seat == '.')
         return false;

      auto adjacentSeats = getAdjacentSeats(map, x, y);
      if (seat == 'L' && adjacentSeats.find('#') == string::npos)
         return true;

      auto occupiedSeats = count(adjacentSeats.begin(), adjacentSeats.end(), '#');
      if (seat == '#' && occupiedSeats >= 4)
         return true;

      return false;
   }

   void runRounds(vector<string>* map, bool needsSwap(vector<string>* map, const int x, const int y)) {
      vector<pair<int, int>> swaps;
      for (auto y = 0; y < map->size(); y++)
         for (auto x = 0; x < map->at(y).size(); x++)
            if (needsSwap(map, x, y))
               swaps.push_back(make_pair(x, y));

      if (swaps.size() == 0)
         return;

      for (const auto& swap : swaps)
         map->at(swap.second).at(swap.first) = map->at(swap.second).at(swap.first) == '#' ? 'L' : '#';

      return runRounds(map, needsSwap);
   }

   TEST_CASE("Day 11 - Part 1 from https://adventofcode.com/2020/day/11") {
      auto mapData = util::loadInputFile("day11-input.txt");

      runRounds(&mapData, needsSwapDueToAdjacentSeats);
      auto result = accumulate(mapData.begin(), mapData.end(), 0U,
         [](const auto sum, auto& line) {
            return sum + count(line.begin(), line.end(), '#');
         });
      
      REQUIRE(result == 2283);
   }

   bool hasOccupiedSeat(vector<string>* map, const int x, const int y, void move(int* x1, int* y1)) {
      auto x1 = x;
      auto y1 = y;
      move(&x1, &y1);

      if (x1 < 0 || y1 < 0 || x1 > map->at(0).size() - 1 || y1 > map->size() - 1)
         return false;

      if (map->at(y1).at(x1) == '#')
         return true;

      if (map->at(y1).at(x1) == 'L')
         return false;
      
      return hasOccupiedSeat(map, x1, y1, move);
   }

   bool needsSwapDueToFirstVisibleSeat(vector<string>* map, const int x, const int y) {
      auto seat = map->at(y).at(x);
      if (seat == '.')
         return false;

      auto occupiedSeats = 0U;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)--;(*y1)--; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*y1)--; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)++;(*y1)--; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)--; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)++; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)--;(*y1)++; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*y1)++; })) occupiedSeats++;
      if (hasOccupiedSeat(map, x, y, [](int* x1, int* y1){ (*x1)++;(*y1)++; })) occupiedSeats++;

      if (seat == 'L' && occupiedSeats == 0)
         return true;

      if (seat == '#' && occupiedSeats >= 5)
         return true;

      return false;
   }

   TEST_CASE("Day 11 - Part 2 from https://adventofcode.com/2020/day/11#part2") {
      auto mapData = util::loadInputFile("day11-input.txt");

      runRounds(&mapData, needsSwapDueToFirstVisibleSeat);
      auto result = accumulate(mapData.begin(), mapData.end(), 0U,
         [](const auto sum, auto& line) {
            return sum + count(line.begin(), line.end(), '#');
         });
      
      REQUIRE(result == 2054);
   }
}
