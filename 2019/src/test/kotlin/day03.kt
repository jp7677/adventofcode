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
        val wireData = Util.getInputAsListOfString("day03-input.txt")

        val wireLocations = walkPaths(wireData)

        val distance = wireLocations.first()
            .intersect(wireLocations.last())
            .minOf { it.toManhattenDistance() }

        assertEquals(245, distance)
    }

    @Test
    fun runPart02() {
        val wireData = Util.getInputAsListOfString("day03-input.txt")

        val wireLocations = walkPaths(wireData)

        val distance = wireLocations.first()
            .intersect(wireLocations.last())
            .map { wireLocations.first().indexOf(it) + wireLocations.last().indexOf(it) + 2 }
            .minOf{ it }

        assertEquals(48262, distance)
    }

    private fun walkPaths(wireData: List<String>) = wireData
        .map { wires ->
            wires
                .split(",")
                .map { it.toPath() }
                .fold(listOf(Location(0, 0))) { locations, path ->
                    locations + walk(locations.last(), path)
                }
                .filter { it != Location(0, 0) }
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