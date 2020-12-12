#include <fstream>

using namespace std;

inline vector<string> loadInputFile(string fileName) {
   vector<string> grid;
   fstream inputStream;
   
   inputStream.open(fileName, ios::in);
   if (!inputStream.is_open())
      throw("file is not open");

   string line;
   while(getline(inputStream, line))
      grid.push_back(line);

   inputStream.close();
   return grid;
}

inline vector<string> split(string delimitedString, char delimiter) {
   istringstream ss(delimitedString);
	string token;
   vector<string> result;
	while(std::getline(ss, token, delimiter))
		result.push_back(token);

   return result;
}