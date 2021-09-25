import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {
    data class Coord(val x: Int, val y: Int)

    @Test
    fun runPart01() {
        val coords = getInputAsCoords()

        val detected = coords.maxOf {
            coords
                .filter { other -> it != other }
                .groupBy { other -> it.angleTo(other) }
                .count()
        }

        assertEquals(274, detected)
    }

    @Test
    fun runPart02() {
        val coords = getInputAsCoords()

        val laser = coords.maxByOrNull {
            coords
                .filter { other -> it != other }
                .groupBy { other -> it.angleTo(other) }
                .count()
        } ?: throw IllegalStateException()

        val asteroids = coords
            .filter { it != laser }
            .sortedBy { laser.angleTo(it) }
            .toMutableList()

        val hits = mutableListOf<Coord>()
        while (asteroids.isNotEmpty()) {
            asteroids.onEach {
                if (it.isVisibleFor(laser, asteroids)) hits.add(it)
            }
            asteroids.removeAll(hits)
        }

        val result = hits[200 - 1]
            .let { it.x * 100 + it.y }
            .toInt()

        assertEquals(305, result)
    }

    private fun getInputAsCoords() = Util.getInputAsListOfString("day10-input.txt")
        .flatMapIndexed { indexY, line -> line.mapIndexed { indexX, char -> Triple(indexX, indexY, char) } }
        .filter { it.third == '#' }
        .map { Coord(it.first, it.second) }

    private fun Coord.isVisibleFor(other: Coord, coords: List<Coord>) =
        coords.none {
            other.angleTo(this) == other.angleTo(it) && other.distanceTo(it) < other.distanceTo(this)
        }

    private fun Coord.angleTo(other: Coord) =
        Math.toDegrees(atan2((other.y - this.y).toDouble(), (other.x - this.x).toDouble()))
            .plus(90)
            .let { if (it < 0) it + 360 else it }

    private fun Coord.distanceTo(other: Coord) = hypot((this.x - other.x).toDouble(), (this.y - other.y).toDouble())
}
