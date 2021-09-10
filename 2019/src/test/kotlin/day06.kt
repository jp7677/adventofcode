import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun runPart01() {
        val map = getInputAsMap()

        val result = map
            .flatMap { listOf(it.first, it.second) }
            .distinct()
            .sumOf { map.traverseOrbits(it).count() }

        assertEquals(241064, result)
    }

    @Test
    fun runPart02() {
        val map = getInputAsMap()

        val result = listOf("YOU", "SAN")
            .map { map.traverseOrbits(it).toList() }
            .let { it.first().count() + it.last().count() - 2 - 2 * (it.first() intersect it.last()).count() }

        assertEquals(418, result)
    }

    private fun List<Pair<String, String>>.traverseOrbits(orbit: String) =
        generateSequence(singleOrNull { it.second == orbit }) { it2 -> firstOrNull { it.second == it2.first } }

    private fun getInputAsMap() = Util.getInputAsListOfString("day06-input.txt")
        .map { it.split(")") }
        .map { Pair(it.first(), it.last()) }
}