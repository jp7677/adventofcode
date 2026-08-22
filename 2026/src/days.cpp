#include "aoc_private.h"
#include "fixture.h"
#include "util.h"

TEST_CASE_PERSISTENT_FIXTURE(aoc::Fixture, "AoC 2026") {
    SECTION("Day 00") {
        SECTION("Part 1") {
            auto input = aoc::util::readInput("day00") //
                | std::views::transform([](auto& line) { return std::stoul(line); })
                | std::ranges::to<std::vector<uint32_t>>();

            glm::uint32 N = input.size(); // number of input elements
            auto inputSize = N * sizeof(glm::uint32);
            auto outputSize = sizeof(glm::uint32);

            glm::uint32 output;
            vulkan.launchShader(input.data(), inputSize, &output, outputSize, "day00-1", "main", N);
            REQUIRE(output == 15u);
        }

        SECTION("Part 2") {
            struct Light {
                glm::ivec3 position;
                glm::uint32 intensity;
            };

            auto input = aoc::util::readInput("day00") //
                | std::views::transform([](auto& line) {
                      auto e = line | std::views::split(',') | std::ranges::to<std::vector<std::string>>();
                      auto coord = glm::ivec3(std::stoul(e[0]), std::stoul(e[1]), std::stoul(e[2]));
                      auto intensity = static_cast<glm::uint32>(std::stoul(e[3]));
                      return Light{.position = coord, .intensity = intensity};
                  })
                | std::ranges::to<std::vector<Light>>();

            glm::uint32 N = input.size(); // number of input elements
            auto inputSize = N * sizeof(Light);
            auto outputSize = sizeof(glm::uint32);

            glm::uint32 output;
            vulkan.launchShader(input.data(), inputSize, &output, outputSize, "day00-2", "main", N);
            REQUIRE(output == 158u);
        }
    }
}
