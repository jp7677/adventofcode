import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day07-input.txt", ",").toTypedArray()

        val signal = listOf(0, 1, 2 ,3 , 4)
            .permutations()
            .maxOf {
                it.fold(0) { input, phase -> IntCodeComputer(memory, phase).run(input) }
            }

        assertEquals (18812, signal)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfInt("day07-input.txt", ",").toTypedArray()

        val signal = listOf(9, 7, 8 ,5 , 6)
            .permutations()
            .maxOf { phases ->
                phases
                    .map { IntCodeComputer(memory.copyOf(), it) }
                    .let { amps ->
                        generateSequence (0) {
                            val signal = amps.fold(it) { input, amp -> amp.run(input) }
                            if (amps.all { amp -> amp.running }) signal else null
                        }.last()
                    }
            }

        assertEquals (25534964, signal)
    }
}