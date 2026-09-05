#pragma once

#include "aoc_private.h"

namespace aoc::util {

inline std::vector<uint32_t> getShaderCode(const std::string_view name) {
    auto shaderPathEnv = std::getenv("SHADER_PATH");
    REQUIRE(shaderPathEnv);
    auto shaderPath = std::string(shaderPathEnv);
    REQUIRE_FALSE(shaderPath.empty());
    if (shaderPath.ends_with('/'))
        shaderPath.pop_back();

    auto filePath = shaderPath + "/" + std::string(name) + ".comp.spv";
    INFO("Loading shader: " << std::filesystem::current_path().c_str() << "/" << filePath);

    auto file = std::ifstream(filePath, std::ios::ate | std::ios::binary);
    REQUIRE_FALSE(file.fail());

    auto fileSize = file.tellg();
    auto spirvCode = std::vector<uint32_t>(fileSize / sizeof(uint32_t));
    file.seekg(0);
    file.read(reinterpret_cast<char*>(spirvCode.data()), fileSize);
    return spirvCode;
}

inline std::vector<std::string> readInput(const std::string_view name) {
    auto dataPathEnv = std::getenv("DATA_PATH");
    REQUIRE(dataPathEnv);
    auto dataPath = std::string(dataPathEnv);
    REQUIRE_FALSE(dataPath.empty());
    if (dataPath.ends_with('/'))
        dataPath.pop_back();

    auto filePath = dataPath + "/" + std::string(name) + "-input.txt";
    INFO("Loading input data: " << std::filesystem::current_path().c_str() << "/" << filePath);
    std::ifstream file(filePath);
    REQUIRE(file.is_open());

    auto input = std::vector<std::string>();
    std::string line;
    while (std::getline(file, line))
        input.push_back(line);

    file.close();

    return input;
}

} // namespace aoc::util