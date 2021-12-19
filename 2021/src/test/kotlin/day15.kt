import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15 {
    data class Coord(val x: Int, val y: Int)
    data class Risk(val coord: Coord, val level: Int)

    data class Travel(val distance: Int?, val previous: Coord?)
    private val directions = listOf(Coord(0, -1), Coord(1, 0), Coord(0, 1), Coord(-1, -1))

    @Test
    fun `run part 01`() {
        val grid = getGrid()

        val nodes = mutableMapOf<Coord, Travel>()
        grid.forEach { nodes[it.coord] = Travel(null, null) }

        nodes[Coord(0, 0)] = Travel(0, null)
        while (nodes.any { it.value.distance == null }) {
            val current = nodes.minByOrNull { it.value.distance ?: Int.MAX_VALUE } ?: throw IllegalStateException()

            directions
                .mapNotNull { direction ->
                    grid.singleOrNull { risk ->
                        risk.coord.x == current.key.x + direction.x && risk.coord.y == current.key.y + direction.y
                    }
                        ?.coord
                }.forEach { coord ->
                    nodes[coord]
                        ?.let { node ->
                            val distance = (current.value.distance ?: 0) + grid.single { it.coord == coord }.level
                            if ((node.distance ?: Int.MAX_VALUE ) > distance) {
                                nodes[coord] = Travel(distance, current.key)
                            }
                        }
                }

            if (current.key == Coord(99, 99))
                break

            nodes.remove(current.key)
        }

        val totalRisk = nodes[Coord(99,99)]?.distance

        assertEquals(720, totalRisk)

    }

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