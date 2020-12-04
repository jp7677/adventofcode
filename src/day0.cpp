#include<iostream>

#define CATCH_CONFIG_MAIN
#include "../inc/catch_amalgamated.hpp"

using namespace std;

int helloWorld()
{
   cout << "Hello World!\n";
   return 0;
}

TEST_CASE("Hello World") {
   REQUIRE( helloWorld() == 0);
}
