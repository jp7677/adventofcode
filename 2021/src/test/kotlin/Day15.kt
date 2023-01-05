import java.lang.IllegalStateException
import java.util.PriorityQueue
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15 {
    data class Coord(val x: Int, val y: Int)
    private data class QueueItem(val coord: Coord, val totalDistance: Int, val priority: Int)
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
        val queue = PriorityQueue<QueueItem>(1, compareBy { it.priority })
            .apply { offer(QueueItem(Coord(0, 0), 0, end.x + end.y)) }
        val totals = mutableMapOf(Coord(0, 0) to 0)
        val visited = mutableSetOf<Coord>()

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            directions
                .map { direction -> Coord(current.coord.x + direction.x, current.coord.y + direction.y) }
                .filterNot { coord -> visited.contains(coord) }
                .mapNotNull { coord -> this[coord]?.let { risk -> coord to risk } }
                .forEach { (coord, risk) ->
                    val newTotalRisk = current.totalDistance + risk
                    val knownTotalRisk = totals[coord] ?: Int.MAX_VALUE
                    if (newTotalRisk < knownTotalRisk) {
                        totals[coord] = newTotalRisk
                        queue.offer(QueueItem(coord, newTotalRisk, newTotalRisk + coord.manhattenDistance(end)))
                    }
                }

            if (current.coord == end) break
            visited.add(current.coord)
        }

        return totals[end] ?: throw IllegalStateException()
    }

    private fun Coord.manhattenDistance(end: Coord) = (end.x - x).absoluteValue + (end.y - y).absoluteValue

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
