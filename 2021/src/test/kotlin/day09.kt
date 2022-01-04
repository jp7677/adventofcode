import kotlin.test.Test
import kotlin.test.assertEquals

class Day09 {
    data class Coord(val x: Int, val y: Int)
    data class Location(val coord: Coord, val height: Int)
    private val moves = listOf(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1))

    @Test
    fun `run part 01`() {
        val heatmap = getHeatmap()

        val riskLevel = heatmap
            .sumOf {
                it.height.let { height ->
                    if (moves.all { step ->
                            heatmap
                                .getAdjacents(it.coord, step)
                                ?.let { adjacent -> adjacent.height > height } != false
                        })
                        height.inc()
                    else 0
                }
            }

        assertEquals(600, riskLevel)
    }

    @Test
    fun `run part 02`() {
        val heatmap = getHeatmap()

        val result = heatmap
            .fold(listOf<List<Coord>>()) { acc, it ->
                if (it.height != 9 && acc.none { b -> b.contains(it.coord) })
                    acc + listOf(heatmap.getAdjacentLowPoints(it.coord).map { l -> l.coord })
                else
                    acc
            }
            .sortedByDescending { it.count() }
            .take(3)
            .fold(1) { acc, it -> acc * it.count() }

        assertEquals(987840, result)
    }

    private fun List<Location>.getAdjacentLowPoints(
        coord: Coord,
        visited: MutableList<Coord> = mutableListOf()
    ): List<Location> =
        if (visited.contains(coord) || this.from(coord).height == 9)
            listOf()
        else {
            visited.add(coord)
            moves
                .mapNotNull { this.getAdjacents(coord, it) }
                .flatMap { this.getAdjacentLowPoints(it.coord, visited) } + this.from(coord)
        }

    private fun List<Location>.from(coord: Coord) = this
        .single { it.coord.x == coord.x && it.coord.y == coord.y }

    private fun List<Location>.getAdjacents(current: Coord, step: Coord) =
        this.firstOrNull { it.coord.x == current.x + step.x && it.coord.y == current.y + step.y }

    private fun getHeatmap() = Util.getInputAsListOfString("day09-input.txt")
        .map { it.map { c -> c.digitToInt() } }
        .let {
            it
                .first()
                .indices
                .flatMap { x ->
                    List(it.size) { y -> Location(Coord(x, y), it[y][x]) }
                }
        }
}