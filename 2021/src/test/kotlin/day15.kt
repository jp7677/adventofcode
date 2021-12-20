import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15 {
    data class Coord(val x: Int, val y: Int)
    private val directions = listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1))

    @Test
    fun `run part 01`() {
        val map = getMap()

        val totalRisk = findTotalRiskForShortestPath(map)

        assertEquals(720, totalRisk)
    }

    @Test
    fun `run part 02`() {
        val map = getMap()

        val additionalRight = map
            .flatMap {
                (1..4).map { x ->
                    Coord(it.key.x + (x * (map.maxX() + 1)), it.key.y) to
                        if (it.value + x > 9) (it.value + x) - 9 else it.value + x
                }
            }

        val additionalDown = (map + additionalRight)
            .flatMap {
                (1..4).map { y ->
                    Coord(it.key.x, it.key.y + (y * (map.maxY() + 1))) to
                        if (it.value + y > 9) (it.value + y) - 9 else it.value + y
                }
            }

        val totalRisk = findTotalRiskForShortestPath(map + additionalRight + additionalDown)

        assertEquals(3025, totalRisk)
    }

    private fun findTotalRiskForShortestPath(map: Map<Coord, Int>): Int {
        val visited = mutableSetOf<Coord>()
        val totalDistances = mutableMapOf(Coord(0,0) to 0)
        val queue = mutableSetOf(Coord(0,0))

        while (queue.any()) {
            val current = queue
                .associateWith { totalDistances[it] }
                .minByOrNull { it.value ?: 0 } ?: throw IllegalStateException()

            directions
                .mapNotNull { direction ->
                    val coord = Coord(current.key.x + direction.x, current.key.y + direction.y)
                    map[coord]?.let { coord to it }
                }
                .filterNot { visited.contains(it.first)  }
                .forEach { coord ->
                    val distance = (totalDistances[current.key] ?: 0) + coord.second
                    if ((totalDistances[coord.first] ?: Int.MAX_VALUE) > distance)
                        totalDistances[coord.first] = distance

                    queue.add(coord.first)
                }

            visited.add(current.key)
            queue.remove(current.key)
        }

        return totalDistances[Coord(map.maxX(), map.maxY())] ?: throw IllegalStateException()
    }

    private fun Map<Coord, Int>.maxX() = this.maxOf { it.key.x }
    private fun Map<Coord, Int>.maxY() = this.maxOf { it.key.y }

    private fun getMap() = Util.getInputAsListOfString("day15-input.txt")
        .map { it.map { c -> c.digitToInt() } }
        .let {
            it
                .first()
                .indices
                .flatMap { x ->
                    List(it.size) { y -> Coord(x, y) to it[y][x] }
                }
        }
        .toMap()
}