import kotlin.test.Test
import kotlin.test.assertEquals

class Day09 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(program.toLongArray()).runToCompletion(1).last()

        assertEquals (3906448201, output)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(program.toLongArray()).runToCompletion(2).last()

        assertEquals (59785, output)
    }
}