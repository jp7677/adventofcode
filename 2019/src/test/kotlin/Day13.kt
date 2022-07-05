import kotlin.test.Test
import kotlin.test.assertEquals

class Day13 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day13-input.txt", ",")

        val countOfBlockTitles = IntCodeComputer(program.toLongArray())
            .runToCompletion()
            .chunked(3)
            .count { it.last() == 2L }

        assertEquals(253, countOfBlockTitles)
    }
}
