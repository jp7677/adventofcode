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

    template<typename... Chars>
    inline vector<string> split(const string& delimitedString, Chars... delimiters) {
        string search{delimiters...};
        size_t begin, position = 0U;
        vector<string> result;
        while ((begin = delimitedString.find_first_not_of(search, position)) != string::npos) {
            position = delimitedString.find_first_of(search, begin + 1);
            result.push_back(delimitedString.substr(begin, position - begin));
        }

        return result;
    }

    inline string replaceAll(const string& replaceableString, const char a, const char b) {
        auto replacedString(replaceableString);
        replace(replacedString.begin(), replacedString.end(), a, b);
        return replacedString;
    }

    inline string replaceAll(const string& replaceableString, const string& a, const string& b) {
        auto replacedString(replaceableString);
        auto position = replacedString.find(a, 0);
        while (position != string::npos) {
            replacedString.erase(position, a.length());
            replacedString.insert(position, b);
            position = replacedString.find(a, position + b.length());
        }

        return replacedString;
    }

    inline string trim(const string& whitespacedString, const string& whitespace = " \t")
    {
        const auto begin = whitespacedString.find_first_not_of(whitespace);
        if (begin == std::string::npos)
            return string();

        const auto end = whitespacedString.find_last_not_of(whitespace);
        return whitespacedString.substr(begin, end - begin + 1);
    }

    inline string reverse(const string& reversibleString) {
        auto reversed = reversibleString;
        reverse(reversed.begin(), reversed.end());
        return reversed;
    }

    inline constexpr ushort numberOfDigits(const uint number) {
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
