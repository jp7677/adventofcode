import kotlin.test.Test
import kotlin.test.assertEquals

class Day01 {

    @Test
    fun `run part 01`() {
        val sweeps = Util.getInputAsListOfInt("day01-input.txt")

        val increments = sweeps
            .zipWithNext()
            .count { it.second > it.first }

        assertEquals(1162, increments)
    }

    @Test
    fun `run part 02`() {
        val sweeps = Util.getInputAsListOfInt("day01-input.txt")

        val increments = sweeps
            .windowed(3, 1)
            .map { it.sum() }
            .zipWithNext()
            .count { it.second > it.first }

        assertEquals(1190, increments)
    }
}