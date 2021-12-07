import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun `run part 01`() {
        val positions = Util.getInputAsListOfInt("day07-input.txt", ",")

        val min = positions.minOrNull() ?: throw IllegalStateException()
        val max = positions.maxOrNull() ?: throw IllegalStateException()
        val minDistance = (min..max)
            .minOfOrNull { distance ->
                positions.fold (0) { acc, it -> acc + abs(distance - it) }
            } ?: throw IllegalStateException()

        assertEquals(356992, minDistance)
    }
}