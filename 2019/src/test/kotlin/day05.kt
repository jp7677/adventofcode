import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day05-input.txt", ",").toLongArray()

        val output = IntCodeComputer(program).runToCompletion(1).last()

        assertEquals (4601506, output)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day05-input.txt", ",").toLongArray()

        val output = IntCodeComputer(program).runToCompletion(5).last()

        assertEquals (5525561, output)
    }
}