import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",").toTypedArray()

        val output = IntCodeComputer(memory).runUntilExit(1).last()

        assertEquals (4601506, output)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",").toTypedArray()

        val output = IntCodeComputer(memory).runUntilExit(5).last()

        assertEquals (5525561, output)
    }
}