#define CATCH_CONFIG_MAIN
#include "../inc/catch.hpp"

#define BACKWARD_HAS_LIBUNWIND 1
#define BACKWARD_HAS_DW 1
#include "../inc/backward.hpp"

[[maybe_unused]] backward::SignalHandling sh;
