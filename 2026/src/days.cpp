#include "aoc_private.h"
#include "fixture.h"
#include "util.h"

TEST_CASE_PERSISTENT_FIXTURE(aoc::Fixture, "AoC 2026") {
    SECTION("Day 00") {
        SECTION("Part 01") {
            auto input = aoc::util::readInput("day00") //
                | std::views::transform([](auto& s) { return std::stoul(s); })
                | std::ranges::to<std::vector<uint32_t>>();

            glm::uint32 N = input.size(); // number of input elements
            auto inputSize = N * sizeof(glm::uint32);
            auto outputSize = sizeof(glm::uint32);

            glm::uint32 output;
            vulkan.launchShader(input.data(), inputSize, &output, outputSize, "day00", "main", N);
            REQUIRE(output == 15u);
        }
    }
}
