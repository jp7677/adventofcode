import kotlin.IllegalArgumentException
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03 {

    enum class Direction { R, L, U, D }
    data class Location(val x: Int, val y: Int) { fun toManhattenDistance() = x.absoluteValue + y.absoluteValue }
    data class Path(val direction: Direction, val length: Int)

    @Test
    fun runPart01() {
        val wires = Util.getInputAsListOfString("day03-input.txt")

        val locations = trace(wires)

        val distance = (locations.first() intersect locations.last().toSet())
            .filter { it != Location(0, 0) }
            .minOf { it.toManhattenDistance() }

        assertEquals(245, distance)
    }

    @Test
    fun runPart02() {
        val wires = Util.getInputAsListOfString("day03-input.txt")

        val locations = trace(wires)

        val distance = (locations.first() intersect locations.last().toSet())
            .filter { it != Location(0, 0) }
            .minOf { locations.first().indexOf(it) + locations.last().indexOf(it) }

        assertEquals(48262, distance)
    }

    private fun trace(wires: List<String>) = wires
        .map { wire ->
            wire
                .split(",")
                .map { it.toPath() }
                .fold(listOf(Location(0, 0))) { locations, path ->
                    locations + traverse(locations.last(), path)
                }
        }

    private fun traverse(start: Location, path: Path) = when (path.direction) {
        Direction.R -> (1..path.length).map { Location(start.x + it, start.y) }
        Direction.L -> (1..path.length).map { Location(start.x - it, start.y) }
        Direction.U -> (1..path.length).map { Location(start.x, start.y + it) }
        Direction.D -> (1..path.length).map { Location(start.x, start.y - it) }
    }

    private fun String.toPath() = Path(
        when (get(0)) {
            'R' -> Direction.R
            'L' -> Direction.L
            'U' -> Direction.U
            'D' -> Direction.D
            else -> throw IllegalArgumentException()
        },
        substring(1).toInt()
    )
}
