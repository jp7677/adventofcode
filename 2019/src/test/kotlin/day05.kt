import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",").toTypedArray()

        val output = IntCodeComputer(memory).run(1)

        assertEquals (4601506, output)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",").toTypedArray()

        val output = IntCodeComputer(memory).run(5)

        assertEquals (5525561, output)
    }
}