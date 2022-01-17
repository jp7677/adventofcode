import kotlin.test.Test
import kotlin.test.assertEquals

class Day11 {
    data class Coord(val x: Int, val y: Int)
    data class Octopus(val coord: Coord, var energy: Int)
    private val flashArea =
        listOf(
            Coord(-1, -1), Coord(0, -1), Coord(1, -1),
            Coord(-1, 0), Coord(0, 0), Coord(1, 0),
            Coord(-1, 1), Coord(0, 1), Coord(1, 1)
        )

    @Test
    fun `run part 01`() {
        val grid = getGrid()

        val flashes = (1..100).sumOf { runDay(grid) }

        assertEquals(1601, flashes)
    }

    @Test
    fun `run part 02`() {
        val grid = getGrid()

        val day = generateSequence(1) { if (runDay(grid) != grid.count()) it.inc() else null }
            .last()

        assertEquals(368, day)
    }

    private fun runDay(grid: List<Octopus>) = grid
        .onEach { it.energy++ }
        .also { it.processFlashing() }
        .filter { it.energy > 9 }
        .onEach { it.energy = 0 }
        .count()

    private fun List<Octopus>.processFlashing() {
        while (true) {
            this.firstOrNull { it.energy == 10 }
                ?.let {
                    flashArea.forEach { coord ->
                        this
                            .firstOrNull { adjacent ->
                                adjacent.coord.x == it.coord.x + coord.x && adjacent.coord.y == it.coord.y + coord.y
                            }
                            ?.let { octopus ->
                                octopus.energy = if (octopus == it) 11
                                else if (octopus.energy == 10) 10
                                else octopus.energy.inc()
                            }
                    }
                } ?: return
        }
    }

    private fun getGrid() = Util.getInputAsListOfString("day11-input.txt")
        .map { it.map { c -> c.digitToInt() } }
        .let {
            it
                .first()
                .indices
                .flatMap { x ->
                    List(it.size) { y -> Octopus(Coord(x, y), it[y][x]) }
                }
        }
}
