#define CATCH_CONFIG_MAIN
#define CATCH_CONFIG_NO_POSIX_SIGNALS
#include <catch2/catch.hpp>

#define BACKWARD_HAS_DW 1
#define BACKWARD_HAS_LIBUNWIND 1
#include "../inc/backward.hpp"

namespace aoc {
    [[maybe_unused]] backward::SignalHandling sh;
}
