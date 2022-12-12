import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Coord(val x: Int, val y: Int)
private val directions = listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1))

class Day12 : StringSpec({
    "puzzle part 01" {
        val map = getMap()
        val start = map.toList().single { it.second == S }.first
        val end = map.toList().single { it.second == E }.first

        val path = findTotalRiskForShortestPath(map, start, end)

        path shouldBe 456
    }

    "puzzle part 02" {
        val map = getMap()
        val end = map.toList().single { it.second == E }.first

        val minPath = map.filter { it.value <= 1 }
            .minOf { findTotalRiskForShortestPath(map, it.key, end) ?: Int.MAX_VALUE }

        minPath shouldBe 454
    }
})

private fun findTotalRiskForShortestPath(map: Map<Coord, Int>, start: Coord, end: Coord): Int? {
    val queue = mutableMapOf(start to 0)
    val totals = mutableMapOf(start to 0)
    val visited = mutableSetOf<Coord>()

    while (queue.any()) {
        val current = queue.minByOrNull { it.value } ?: throw IllegalStateException()

        directions
            .map { direction -> Coord(current.key.x + direction.x, current.key.y + direction.y) }
            .filterNot { coord -> visited.contains(coord) }
            .mapNotNull { coord -> map[coord]?.let { coord to it } }
            .filter { (_, distance) -> distance <= map[current.key]!! + 1 }
            .forEach { (coord, _) ->
                val totalDistance = current.value + 1
                val knownTotalDistance = totals[coord] ?: Int.MAX_VALUE
                if (totalDistance < knownTotalDistance) {
                    totals[coord] = totalDistance
                    queue[coord] = totalDistance
                } else
                    queue[coord] = knownTotalDistance
            }

        if (current.key == end)
            break

        visited.add(current.key)
        queue.remove(current.key)
    }

    return totals.filterKeys { it == end }.toList().firstOrNull()?.second
}

private const val S = 0
private const val E = ('z'.code - 96) + 1

private fun getMap() = getPuzzleInput("day12-input.txt")
    .map {
        it.map { c ->
            when (c) {
                'S' -> S
                'E' -> E
                else -> c.code - 96
            }
        }
    }
    .toList()
    .let {
        it
            .first()
            .indices
            .flatMap { x ->
                List(it.size) { y -> Coord(x, y) to it[y][x] }
            }
    }
    .toMap()
