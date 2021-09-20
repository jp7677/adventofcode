import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(program.toLongArray()).run(1).last()

        assertEquals (3906448201, output)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day09-input.txt", ",")

        val output = IntCodeComputer(program.toLongArray()).run(2).last()

        assertEquals (59785, output)
    }
}