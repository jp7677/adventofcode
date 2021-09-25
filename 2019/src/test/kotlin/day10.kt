import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {
    data class Coord(val x: Double, val y: Double) {
        constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
    }

    @Test
    fun runPart01() {
        val coords = getInputAsCoords()

        val detected = coords.maxOf {
            coords
                .filter { other -> it != other }
                .count { other -> it.isVisibleFor(other, coords) }
        }

        assertEquals(274, detected)
    }

    @Test
    fun runPart02() {
        val coords = getInputAsCoords()

        val laser = coords.maxByOrNull {
            coords
                .filter { other -> it != other }
                .count { other -> it.isVisibleFor(other, coords) }
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
        (this.pathTo(other).trim() intersect coords).isEmpty()

    private fun Coord.pathTo(other: Coord) =
        if (this.x != other.x)
            (this.x towards other.x)
                .map { Coord(it, solveLinearEquationFromTwoPoints(it, this, other)) }
                .filter { it.y.isWholeNumber() }
        else
            (this.y towards other.y).map { Coord(this.x, it) }

    private fun solveLinearEquationFromTwoPoints(x: Double, p: Coord, q: Coord) =
        p.y + (x - p.x) / (q.x - p.x) * (q.y - p.y)

    private fun Coord.angleTo(asteroid: Coord) =
        Math.toDegrees(kotlin.math.atan2(asteroid.y - this.y, asteroid.x - this.x))
            .plus(90)
            .let { if (it < 0) it + 360 else it }
}