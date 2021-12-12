import kotlin.test.Test
import kotlin.test.assertEquals

class Day12 {
    data class Connection(val from: String, val to: String)

    @Test
    fun `run part 01`() {
        val connections = getCaveConnections()

        val path = mutableListOf<List<String>>()
        connections.buildAllPaths("start", 1, path)

        val count = path.count()

        assertEquals(4720, count)
    }

    @Test
    fun `run part 02`() {
        val connections = getCaveConnections()

        val paths = mutableListOf<List<String>>()
        connections.buildAllPaths("start", 2, paths)

        val count = paths.count()

        assertEquals(147848, count)
    }

    private fun Set<Connection>.buildAllPaths(
        element: String,
        maxVisits: Int = 1,
        acc: MutableList<List<String>>,
        path: List<String> = listOf()
    ) {
        if (element == "end")
            acc += path

        if (element == "end" || path.maxVisitsExceeded(maxVisits))
            return

        this
            .filter { it.from == element  }
            .map { it.to }
            .forEach { to -> this.buildAllPaths(to, maxVisits, acc, path + to) }
    }

    private fun List<String>.maxVisitsExceeded(maxVisits: Int) = this
        .filter { it.all { c -> c.isLowerCase() } }
        .groupingBy { it }.eachCount()
        .let {
            it.any { c -> c.value > maxVisits } || (it.count { c -> c.value == 2 } > 1)
        }

    private fun getCaveConnections(name: String = "day12-input.txt") = Util.getInputAsListOfString(name)
        .map { it.split("-").let { s -> Connection(s.first(), s.last()) } }
        .let { connections ->
            connections
                .map { Connection(it.to, it.from) }
                .union(connections)
                .filter { it.from != "end" && it.to != "start" }
                .toSet()
        }
}