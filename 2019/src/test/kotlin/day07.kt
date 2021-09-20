import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day07-input.txt", ",").toLongArray()

        val signal = listOf(0L, 1L, 2L, 3L , 4L)
            .permutations()
            .maxOf {
                it.fold(0L) { input, phase ->
                    IntCodeComputer(program, phase).runWithBreakOnOutput(input) ?: throw IllegalStateException()
                }
            }

        assertEquals (18812, signal)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day07-input.txt", ",").toLongArray()

        val signal = listOf(9L, 7L, 8L, 5L , 6L)
            .permutations()
            .maxOf { phases ->
                phases
                    .map { IntCodeComputer(program.copyOf(), it) }
                    .let { amps ->
                        generateSequence(0L) {
                            amps.fold(it as? Long) { input, amp ->
                                if (input != null) amp.runWithBreakOnOutput(input) else amp.runWithBreakOnOutput()
                            }
                        }.last()
                    }
            }

        assertEquals (25534964, signal)
    }
}