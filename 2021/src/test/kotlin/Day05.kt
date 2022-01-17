import Util.towards
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {
    data class Coord(val x: Int, val y: Int)

    @Test
    fun `run part 01`() {
        val overlaps = getCoords().calcOverlaps()

        assertEquals(8111, overlaps)
    }

    @Test
    fun `run part 02`() {
        val overlaps = getCoords().calcOverlaps(true)

        assertEquals(22088, overlaps)
    }

    private fun getCoords() = Util.getInputAsListOfString("day05-input.txt")
        .map {
            it.split(" ", ",").let { s ->
                Coord(s[0].toInt(), s[1].toInt()) to Coord(s[3].toInt(), s[4].toInt())
            }
        }

    private fun List<Pair<Coord, Coord>>.calcOverlaps(includeDiagonals: Boolean = false) = this
        .flatMap {
            if (it.first.x == it.second.x)
                (it.first.y towards it.second.y).map { y -> Coord(it.first.x, y) }
            else if (it.first.y == it.second.y)
                (it.first.x towards it.second.x).map { x -> Coord(x, it.first.y) }
            else if (includeDiagonals && abs(it.first.x - it.second.x) == abs(it.first.y - it.second.y))
                (it.first.x towards it.second.x).mapIndexed { index, x ->
                    Coord(x, (it.first.y towards it.second.y).elementAt(index))
                }
            else
                listOf()
        }
        .groupingBy { it }
        .eachCount()
        .count { it.value >= 2 }
}
