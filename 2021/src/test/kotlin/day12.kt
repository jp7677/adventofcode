import kotlin.test.Test
import kotlin.test.assertEquals

class Day12 {
    data class Path(val from: String, val to: String)
    data class Node(val from: String, val tos: List<String>)

    @Test
    fun `run part 01`() {
        val tree = getCavePathsTree()

        val journeys = mutableListOf<List<String>>()
        tree.walk("start", 1, listOf("START"), journeys)

        val count = journeys
            .onEach { println(it) }
            .count()

        assertEquals(4720, count)
    }

    @Test
    fun `run part 02`() {
        val tree = getCavePathsTree()

        val journeys = mutableListOf<List<String>>()
        tree.walk("start", 2, listOf("START"), journeys)

        val count = journeys.count { it.none { s -> s == "start" } }

        assertEquals(147848, count)
    }

    private fun Set<Node>.walk(element: String, maxCount: Int = 1, journey: List<String> = listOf(), acc: MutableList<List<String>>) {
        if (element == "end")
            acc.add(journey)

        if (element == "end"
            || (journey
                    .filter { it.all { c -> c.isLowerCase() } }
                    .groupingBy { it }.eachCount()
                    .let {
                        it.any { c -> c.value > maxCount } || (it.count { c -> c.value == 2 } > 1)
                    }))
            return

        this
            .single { it.from == element }
            .tos.forEach { to -> this.walk(to, maxCount, journey + to, acc) }
    }

    private fun getCavePathsTree(): Set<Node> {
        val cavesMap = Util.getInputAsListOfString("day12-input.txt")
            .map { it.split("-").let { s -> Path(s.first(), s.last()) } }

        val paths = cavesMap
            .filter { it.from != "start" && it.to != "end" }
            .map { Path(it.to, it.from) }
            .union(cavesMap)

        return paths
            .map { paths.getDestinations(it.from) }
            .distinct()
            .toSet()
    }

    private fun Set<Path>.getDestinations(element: String) =
        Node(
            element,
            this
                .filter { it.from == element }
                .map { it.to })
}