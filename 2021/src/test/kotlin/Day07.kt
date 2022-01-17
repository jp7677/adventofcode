import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07 {

    @Test
    fun `run part 01`() {
        val positions = Util.getInputAsListOfInt("day07-input.txt", ",")

        val minDistance = getDistances(positions)
            .minOfOrNull { distance ->
                positions.sumOf { abs(distance - it) }
            } ?: throw IllegalStateException()

        assertEquals(356992, minDistance)
    }

    @Test
    fun `run part 02`() {
        val positions = Util.getInputAsListOfInt("day07-input.txt", ",")

        val minDistance = getDistances(positions)
            .minOfOrNull { distance ->
                positions.sumOf { (1..abs(distance - it)).sum() }
            } ?: throw IllegalStateException()

        assertEquals(101268110, minDistance)
    }

    private fun getDistances(positions: List<Int>) =
        (positions.minOrNull() ?: throw IllegalStateException())..(positions.maxOrNull() ?: throw IllegalStateException())
}
