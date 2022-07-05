import kotlin.test.Test
import kotlin.test.assertEquals

class Day01 {

    @Test
    fun runPart01() {
        val fuel = Util.getInputAsListOfInt("day01-input.txt")
            .sumOf { (it / 3) - 2 }

        assertEquals(3239503, fuel)
    }

    @Test
    fun runPart02() {
        val fuel = Util.getInputAsListOfInt("day01-input.txt")
            .sumOf { calculateFuel(it) }

        assertEquals(4856390, fuel)
    }

    private tailrec fun calculateFuel(mass: Int, sum: Int = 0): Int {
        val fuel = (mass / 3) - 2
        return if (fuel > 0) calculateFuel(fuel, sum + fuel) else sum
    }
}
