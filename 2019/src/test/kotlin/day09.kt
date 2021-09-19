import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(memory.toLongArray()).runUntilExit(1).last()

        assertEquals (3906448201, output)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(memory.toLongArray()).runUntilExit(2).last()

        assertEquals (59785, output)
    }
}