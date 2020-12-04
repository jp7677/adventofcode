#include<iostream>
#include<vector>

using namespace std;

int main()
{
   cout << "Day 1 from https://adventofcode.com/2020/day/1\n";

   vector<int> list { 1721, 979, 366, 299, 675, 1456 };

   for (auto const element1 : list)
      for (auto const element2 : list)
         if (element1 + element2 == 2020) {
            cout << "Result " << element1 * element2 << "\n";
            return 0;
         }

   return 0;
}
