import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfLong("day07-input.txt", ",").toLongArray()

        val signal = listOf(0L, 1L, 2L, 3L , 4L)
            .permutations()
            .maxOf {
                it.fold(0L) { input, phase -> IntCodeComputer(memory, phase).run(input) ?: throw IllegalStateException() }
            }

        assertEquals (18812, signal)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfLong("day07-input.txt", ",").toLongArray()

        val signal = listOf(9L, 7L, 8L, 5L , 6L)
            .permutations()
            .maxOf { phases ->
                phases
                    .map { IntCodeComputer(memory.copyOf(), it) }
                    .let { amps ->
                        generateSequence(0L) {
                            amps.fold(it as? Long) { input, amp -> if (input != null) amp.run(input) else amp.run() }
                        }.last()
                    }
            }

        assertEquals (25534964, signal)
    }
}