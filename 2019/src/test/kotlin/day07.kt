import kotlin.test.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day07-input.txt", ",").toTypedArray()

        val signal = listOf(0, 1, 2 ,3 , 4)
            .permutations()
            .maxOf {
                it.fold(0) { input, phase ->
                    IntCodeComputer(memory, phase).run(input) }
                }

        assertEquals (18812, signal)
    }
}