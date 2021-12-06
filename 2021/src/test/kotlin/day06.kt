import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun `run part 01`() {
        val countOfAllFish = getPopulation()
            .runDays(80)
            .sum()

        assertEquals(350917, countOfAllFish)
    }

    @Test
    fun `run part 02`() {
        val countOfAllFish = getPopulation()
            .runDays(256)
            .sum()

        assertEquals(1592918715629, countOfAllFish)
    }

    private fun getPopulation(): LongArray {
        val ages = Util.getInputAsString("day06-input.txt")
            .split(",").map { it.toInt() }
            .toList()

        return LongArray(9) { index -> ages.count { it == index }.toLong() }
    }

    private fun LongArray.runDays(days: Int): LongArray {
        repeat(days) {
            val last = this.copyOf()
            this[8] = last[0]
            this[7] = last[8]
            this[6] = last[7] + last[0]
            this[5] = last[6]
            this[4] = last[5]
            this[3] = last[4]
            this[2] = last[3]
            this[1] = last[2]
            this[0] = last[1]
        }
        return this
    }
}