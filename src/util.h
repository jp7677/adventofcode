#include <fstream>
#include <filesystem>

using namespace std;

namespace util {
   inline vector<string> loadInputFile(const string& fileName) {
      vector<string> data;
      fstream inputStream;

      inputStream.open(filesystem::path("data") / fileName, ios::in);
      if (!inputStream.is_open())
         throw runtime_error("file is not open");

      string line;
      while(getline(inputStream, line))
         data.push_back(line);

      inputStream.close();
      return data;
   }

   inline vector<string> split(const string& delimitedString, const char delimiter) {
      istringstream inputStream(delimitedString);
      string token;
      vector<string> result;
      while(getline(inputStream, token, delimiter))
         if(!token.empty())
            result.push_back(token);

      return result;
   }

   inline string replaceAll(const string& replaceableString, const char a, const char b) {
      auto replacedString(replaceableString);
      replace(replacedString.begin(), replacedString.end(), a, b);
      return replacedString;
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

   inline void negate (int& a) {
      a *= -1;
   }

   static const auto numberOfCpuCores = thread::hardware_concurrency();
   inline uint concurrency() {
      return clamp(numberOfCpuCores / 2, 1U, 4U);
   }
}
