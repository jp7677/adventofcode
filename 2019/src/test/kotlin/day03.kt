import java.lang.IllegalArgumentException
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

        val locations = walkPaths(wires)

        val distance = (locations.first() intersect locations.last())
            .filter { it != Location(0, 0) }
            .minOf { it.toManhattenDistance() }

        assertEquals(245, distance)
    }

    @Test
    fun runPart02() {
        val wires = Util.getInputAsListOfString("day03-input.txt")

        val locations = walkPaths(wires)

        val distance = (locations.first() intersect locations.last())
            .filter { it != Location(0, 0) }
            .minOf { locations.first().indexOf(it) + locations.last().indexOf(it) }

        assertEquals(48262, distance)
    }

    private fun walkPaths(wires: List<String>) = wires
        .map { wire ->
            wire
                .split(",")
                .map { it.toPath() }
                .fold(listOf(Location(0, 0))) { locations, path ->
                    locations + walk(locations.last(), path)
                }
        }

    private fun String.toPath(): Path = Path(
        when (this[0]) {
            'R' -> Direction.R
            'L' -> Direction.L
            'U' -> Direction.U
            'D' -> Direction.D
            else -> throw IllegalArgumentException()
        },
        this.substring(1).toInt()
    )

    private fun walk(start: Location, path: Path): List<Location> = when (path.direction){
        Direction.R -> (1..path.length).map { Location(start.x + it, start.y) }
        Direction.L -> (1..path.length).map { Location(start.x - it, start.y) }
        Direction.U -> (1..path.length).map { Location(start.x, start.y + it) }
        Direction.D -> (1..path.length).map { Location(start.x, start.y - it) }
    }
}
