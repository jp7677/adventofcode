import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun `run part 01`() {
        val count = runDays(80)

        assertEquals(350917, count)
    }

    @Test
    fun `run part 02`() {
        val count = runDays(256)

        assertEquals(1592918715629, count)
    }

    private fun runDays(days: Int) = Util.getInputAsListOfInt("day06-input.txt", ",")
        .let { ages ->
            LongArray(9) { index -> ages.count { it == index }.toLong() }
        }
        .also {
            repeat(days) { _ ->
                val new = it[0]
                it.indices.forEach { index -> it[index] = if (index == 8) new else it[index.inc()] }
                it[6] += new
            }
        }
        .sum()
}