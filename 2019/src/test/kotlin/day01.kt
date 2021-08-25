import kotlin.test.Test
import kotlin.test.junit5.JUnit5Asserter as Asserter

class Day01 {

    @Test
    fun runPart01() {
        val result = Util.getInputAsListOfInt("day01-input.txt")
            .sumOf { (it / 3) - 2 }

        Asserter.assertEquals("wrong result", 3239503, result)
    }

    @Test
    fun runPart02() {
        val result = Util.getInputAsListOfInt("day01-input.txt")
            .sumOf { calculateFuel(it) }

        Asserter.assertEquals("wrong result", 4856390, result)
    }

    private tailrec fun calculateFuel(mass: Int, sum: Int = 0): Int {
        val fuel = (mass / 3) - 2
        return if (fuel > 0) calculateFuel(fuel, sum + fuel) else sum
    }
}