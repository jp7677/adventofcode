import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.PriorityQueue

private data class Coord12(val x: Int, val y: Int)
private val directions = listOf(Coord12(1, 0), Coord12(0, 1), Coord12(-1, 0), Coord12(0, -1))

class Day12 : StringSpec({
    "puzzle part 01" {
        val map = getMap()
        val start = map.filterValues { it == S }.keys.single()
        val end = map.filterValues { it == E }.keys.single()

        val path = findShortestPathLength(map, start, end)

        path shouldBe 456
    }

    "puzzle part 02" {
        val map = getMap()
        val end = map.filterValues { it == E }.keys.single()

        val shortestPath = map.filter { it.value <= 1 }
            .mapNotNull { findShortestPathLength(map, it.key, end) }
            .min()

        shortestPath shouldBe 454
    }
})

private fun findShortestPathLength(map: Map<Coord12, Int>, start: Coord12, end: Coord12): Int? {
    val queue = PriorityQueue<Pair<Coord12, Int>>(1, compareBy { (_, distance) -> distance })
        .apply { offer(start to 0) }
    val totals = mutableMapOf(start to 0)
    val visited = mutableSetOf<Coord12>()

    while (queue.isNotEmpty()) {
        val (current, totalDistance) = queue.poll()

        directions
            .map { direction -> Coord12(current.x + direction.x, current.y + direction.y) }
            .filterNot { coord -> visited.contains(coord) }
            .mapNotNull { coord -> map[coord]?.let { coord to it } }.toMap()
            .filterValues { it <= map[current]!! + 1 }
            .keys.forEach { coord ->
                val newTotalDistance = totalDistance + 1
                val knownTotalDistance = totals[coord] ?: Int.MAX_VALUE
                if (newTotalDistance < knownTotalDistance) {
                    totals[coord] = newTotalDistance
                    queue.offer(coord to newTotalDistance)
                }
            }

        if (current == end) break
        visited.add(current)
    }

    return totals.filterKeys { it == end }.toList().firstOrNull()?.second
}

private fun getMap() = getPuzzleInput("day12-input.txt").toList()
    .map {
        it.map { c ->
            when (c) {
                'S' -> S
                'E' -> E
                else -> c.code - 96
            }
        }
    }
    .let {
        it.first().indices.flatMap { x ->
            List(it.size) { y -> Coord12(x, y) to it[y][x] }
        }
    }.toMap()

private const val S = 0
private const val E = ('z'.code - 96) + 1
