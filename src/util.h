#include <fstream>

using namespace std;

namespace util {
   inline vector<string> loadInputFile(const string fileName) {
      vector<string> data;
      fstream inputStream;
      
      inputStream.open(fileName, ios::in);
      if (!inputStream.is_open())
         throw("file is not open");

      string line;
      while(getline(inputStream, line))
         data.push_back(line);

      inputStream.close();
      return data;
   }

   inline vector<string> split(const string delimitedString, const char delimiter) {
      istringstream inputStream(delimitedString);
      string token;
      vector<string> result;
      while(getline(inputStream, token, delimiter))
         result.push_back(token);

      return result;
   }

   inline constexpr ushort numberOfDigits(const uint number)  
   {  
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
}
