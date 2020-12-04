#include "../inc/catch_amalgamated.hpp"

using namespace std;

string helloWorld() {
   return "Hello World!";
}

TEST_CASE("Hello World") {
   REQUIRE( helloWorld() == "Hello World!");
}
