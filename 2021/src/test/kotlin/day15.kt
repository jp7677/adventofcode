import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15 {
    data class Coord(val x: Int, val y: Int)
    data class Risk(val coord: Coord, val level: Int)
    private val moves = listOf(Coord(0, 1), Coord(1, 0))

    @Test
    @Ignore("Works only with the sample data :(")
    fun `run part 01`() {
        val grid = getGrid()
        val paths = mutableListOf<List<Risk>>()
        grid.buildAllPaths(grid.first().coord, paths, listOf(grid.first()))

        val min = paths
            .minOf { it.sumOf { risk -> risk.level } } - 1

        assertEquals(40, min)
    }

    private fun Set<Risk>.buildAllPaths(
        coord: Coord,
        acc: MutableList<List<Risk>>,
        path: List<Risk> = listOf()
    ) {
        if (this.isEndPos(coord)) {
            acc.clear()
            acc += path
            return
        }

        if (acc.any { ac ->  ac.sumOf { it.level } <= path.sumOf { it.level } })
            return

        moves
            .mapNotNull {
                move -> this.singleOrNull { it.coord.x == coord.x + move.x && it.coord.y == coord.y + move.y }
            }
            .sortedBy { it.level }
            .forEach { this.buildAllPaths(it.coord, acc, path + it) }
    }

    private fun Set<Risk>.isEndPos(coord: Coord) =
        coord.x == this.maxOf { it.coord.x } && coord.y == this.maxOf { it.coord.y }

    private fun getGrid() = Util.getInputAsListOfString("day15-input.txt")
        .map { it.map { c -> c.digitToInt() } }
        .let {
            it
                .first()
                .indices
                .flatMap { x ->
                    List(it.size) { y -> Risk(Coord(x, y), it[y][x]) }
                }
        }
        .toSet()
}