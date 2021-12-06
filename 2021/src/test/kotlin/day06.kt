import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun `run part 01`() {
        val population = Util.getInputAsString("day06-input.txt")
            .split(",").map { it.toInt() }
            .toMutableList()

        repeat(80) { _ ->
            val new = population.count { it == 0 }
            population.onEachIndexed { index, it -> population[index] = if (it == 0) 6 else it - 1 }
            repeat(new) { population.add(8) }
        }

        val countOfFish = population.size

        assertEquals(350917, countOfFish)
    }

    @Test
    fun `run part 02`() {
        val ages = Util.getInputAsString("day06-input.txt")
            .split(",").map { it.toInt() }
            .toList()

        val population = LongArray(9) { index -> ages.count { it == index }.toLong() }

        repeat(256) {
            val last = population.copyOf()
            population[8] = last[0]
            population[7] = last[8]
            population[6] = last[7] + last[0]
            population[5] = last[6]
            population[4] = last[5]
            population[3] = last[4]
            population[2] = last[3]
            population[1] = last[2]
            population[0] = last[1]
        }

        val countOfAllFish = population.sum()

        assertEquals(1592918715629, countOfAllFish)
    }
}