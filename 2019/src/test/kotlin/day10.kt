import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {
    data class Coord(val x: Double, val y: Double) {
        constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
    }

    @Test
    fun runPart01() {
        val coords = Util.getInputAsListOfString("day10-input.txt")
            .flatMapIndexed { indexY, line -> line.mapIndexed { indexX, char -> Triple(indexX, indexY, char) } }
            .filter { it.third == '#' }
            .map { Coord(it.first, it.second) }

        val detected = coords.maxOf {
            coords
                .filter { other -> it != other }
                .count { other -> it.isVisibleFor(other, coords) }
        }

        assertEquals(274, detected)
    }

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
}