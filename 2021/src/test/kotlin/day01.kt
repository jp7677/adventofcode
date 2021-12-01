import kotlin.test.Test
import kotlin.test.assertEquals

class Day01 {

    @Test
    fun `run part 01`() {
        val depths = Util.getInputAsListOfInt("day01-input.txt")

        val increments = depths
            .zipWithNext()
            .count { it.second > it.first }

        assertEquals(1162, increments)
    }

    @Test
    fun `run part 02`() {
        val depths = Util.getInputAsListOfInt("day01-input.txt")

        val increments = depths
            .windowed(3, 1)
            .map { it.sum() }
            .zipWithNext()
            .count { it.second > it.first }

        assertEquals(1190, increments)
    }
}