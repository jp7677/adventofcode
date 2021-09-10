import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun runPart01() {
        val map = Util.getInputAsListOfString("day06-input.txt")
            .map { it.split(")") }
            .map { Pair(it[0],it[1]) }

        val result = map
            .flatMap { listOf(it.first, it.second) }
            .distinct()
            .sumOf { map.numberOfOrbits(it) }

        assertEquals(241064, result)
    }

    private tailrec fun List<Pair<String, String>>.numberOfOrbits(orbit: String, sum: Int = 0): Int =
        if (this.any { orbit == it.second })
            numberOfOrbits(this.single { orbit == it.second }.first, sum + 1)
        else
            sum
}