import kotlin.test.Test
import kotlin.test.assertEquals

class Day11 {
    data class Coord(val x: Int, val y: Int)
    data class Octupus(val coord: Coord, var energy: Int)
    private val flashArea =
        listOf(
            Coord(-1, -1), Coord(0, -1), Coord(1, -1),
            Coord(-1, 0), Coord(1, 0),
            Coord(-1, 1), Coord(0, 1), Coord(1, 1))

    @Test
    fun `run part 01`() {
        val grid = getGrid()

        var flashes = 0
        repeat(100) {
            flashes += runDay(grid)
        }

        assertEquals(1601, flashes)
    }

    private fun runDay(grid: List<Octupus>): Int {
        var grid1 = grid
        grid1.onEach { it.energy++ }
        grid1 = processFlashing(grid1).last()
        return grid1
            .filter { it.energy > 9 }
            .onEach { it.energy = 0 }
            .count()
    }

    private fun processFlashing(grid: List<Octupus>) = generateSequence(grid) { intermediateGrid ->
        intermediateGrid
            .firstOrNull { it.energy == 10 }
            ?.let {
                it.energy = 11
                flashArea.onEach { n ->
                    intermediateGrid
                        .firstOrNull { f -> f.coord.x == it.coord.x + n.x && f.coord.y == it.coord.y + n.y }
                        ?.let { o -> o.energy = if (o.energy == 10) 10 else o.energy +1 }
                }
                intermediateGrid
            }
    }

    private fun getGrid() = Util.getInputAsListOfString("day11-input.txt")
        .map { it.map { c -> c.digitToInt() } }
        .let {
            it
                .first()
                .indices
                .flatMap { x ->
                    it.indices.map { y ->
                        Octupus(Coord(x, y), it[y][x])
                    }
                }
        }

    private fun List<Octupus>.println(padding: Int = 0) = this
        .map { it.coord.y }
        .distinct()
        .sorted()
        .onEach { y ->
            this
                .filter { it.coord.y == y }
                .map { it.coord.x }
                .sorted()
                .onEach { x ->
                    print(
                        this
                            .first { it.coord.x == x && it.coord.y == y }
                            .energy.toString()
                            .padStart(padding, ' '))
                }
                .also { kotlin.io.println() }
        }
        .also { kotlin.io.println() }
}