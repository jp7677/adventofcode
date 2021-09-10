import kotlin.test.Test
import kotlin.test.assertEquals

class Day06 {

    @Test
    fun runPart01() {
        val map = getInputAsMap()

        val result = map
            .flatMap { listOf(it.first, it.second) }
            .distinct()
            .sumOf { map.traverseOrbits(it).size }

        assertEquals(241064, result)
    }

    @Test
    fun runPart02() {
        val map = getInputAsMap()

        val result = listOf("YOU", "SAN")
            .map { map.traverseOrbits(it) }
            .let { it.first().count() + it.last().count() - 2 - 2 * (it.first() intersect it.last()).count() }

        assertEquals(418, result)
    }

    private tailrec fun List<Pair<String, String>>.traverseOrbits(
        orbit: String,
        path: MutableList<Pair<String, String>> = mutableListOf()
    ): List<Pair<String, String>> {
        val next = this.firstOrNull { orbit == it.second } ?: return path
        path += next
        return traverseOrbits(this.single { orbit == it.second }.first, path)
    }

    private fun getInputAsMap() = Util.getInputAsListOfString("day06-input.txt")
        .map { it.split(")") }
        .map { Pair(it[0], it[1]) }
}