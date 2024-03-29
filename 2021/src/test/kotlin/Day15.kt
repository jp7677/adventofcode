import java.lang.IllegalStateException
import java.util.PriorityQueue
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15 {
    data class Coord(val x: Int, val y: Int)
    private val directions = listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1))

    @Test
    fun `run part 01`() {
        val map = getMap()

        val totalRisk = map.findTotalRiskForShortestPath()

        assertEquals(720, totalRisk)
    }

    @Test
    fun `run part 02`() {
        val map = getMap().repeatRight(5).repeatDownwards(5)

        val totalRisk = map.findTotalRiskForShortestPath()

        assertEquals(3025, totalRisk)
    }

    private fun Map<Coord, Int>.findTotalRiskForShortestPath(): Int {
        val end = Coord(maxX(), maxY())
        val queue = PriorityQueue<Pair<Coord, Int>>(1, compareBy { (_, risk) -> risk })
            .apply { offer(Coord(0, 0) to 0) }
        val totals = mutableMapOf(Coord(0, 0) to 0)
        val visited = mutableSetOf<Coord>()

        while (queue.isNotEmpty()) {
            val (current, totalRisk) = queue.poll()

            directions
                .map { direction -> Coord(current.x + direction.x, current.y + direction.y) }
                .filterNot { coord -> visited.contains(coord) }
                .mapNotNull { coord -> this[coord]?.let { risk -> coord to risk } }
                .forEach { (coord, risk) ->
                    val newTotalRisk = totalRisk + risk
                    val knownTotalRisk = totals[coord] ?: Int.MAX_VALUE
                    if (newTotalRisk < knownTotalRisk) {
                        totals[coord] = newTotalRisk
                        queue.offer(coord to newTotalRisk)
                    }
                }

            if (current == end) break
            visited.add(current)
        }

        return totals[end] ?: throw IllegalStateException()
    }

    private fun Map<Coord, Int>.repeatRight(tiles: Int): Map<Coord, Int> {
        val maxX = this.maxX()
        return this + this
            .flatMap {
                (1 until tiles).map { x ->
                    Coord(it.key.x + (x * (maxX.inc())), it.key.y) to
                        if (it.value + x > 9) (it.value + x) - 9 else it.value + x
                }
            }
    }

    private fun Map<Coord, Int>.repeatDownwards(tiles: Int): Map<Coord, Int> {
        val maxY = this.maxY()
        return this + this
            .flatMap {
                (1 until tiles).map { y ->
                    Coord(it.key.x, it.key.y + (y * (maxY.inc()))) to
                        if (it.value + y > 9) (it.value + y) - 9 else it.value + y
                }
            }
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
