import kotlin.test.Test
import kotlin.test.assertEquals

class Day12 {
    data class Path(val from: String, val to: String)
    data class Node(val from: String, val tos: List<String>)

    @Test
    fun `run part 01`() {
        val cavesMap = Util.getInputAsListOfString("day12-input.txt")
            .map { it.split("-").let { s -> Path(s.first(), s.last()) } }

        val paths = cavesMap
            .filter { it.from != "start" && it.to != "end" }
            .map { Path(it.to, it.from) }
            .union(cavesMap)

        val tree = paths
            .map { paths.getDestinations(it.from) }
            .distinct()
            .toSet()
            .onEach { println(it) }

        val journeys = mutableListOf<List<String>>()
        tree.walk("start", listOf("START"), journeys)

        val count = journeys.count { it.none { s -> s == "start" } }

        assertEquals(4720, count)
    }

    private fun Set<Node>.walk(element: String, journey: List<String> = listOf(), acc: MutableList<List<String>>) {
        if (element == "end")
            acc.add(journey)

        if (element == "end" || (element.lowercase() == element && journey.dropLast(1).contains(element)))
            return

        this
            .single { it.from == element }
            .tos.forEach { to -> this.walk(to, journey + to, acc) }
    }


    private fun Set<Path>.getDestinations(element: String) =
        Node(
            element,
            this
                .filter { it.from == element }
                .map { it.to })
}