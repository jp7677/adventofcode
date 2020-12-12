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

inline int numberOfDigits(int number)  
{  
   number = abs(number);  
   return (number < 10 ? 1 :   
      (number < 100 ? 2 :   
      (number < 1000 ? 3 :   
      (number < 10000 ? 4 :   
      (number < 100000 ? 5 :   
      (number < 1000000 ? 6 :   
      (number < 10000000 ? 7 :  
      (number < 100000000 ? 8 :  
      (number < 1000000000 ? 9 :  
      10)))))))));  
}
