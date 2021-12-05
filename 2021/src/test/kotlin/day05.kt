import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {
    data class Coord(val x: Int, val y: Int)

    @Test
    fun `run part 01`() {
        val ventEndCoords = Util.getInputAsListOfString("day05-input.txt")
            .map { it.split(" ", ",")
                .let { s ->
                    Coord(s[0].toInt(), s[1].toInt()) to Coord(s[3].toInt(), s[4].toInt()) }
                }

        val overlaps = ventEndCoords
            .flatMap {
                if (it.first.x == it.second.x)
                    (it.first.y towards it.second.y).map { y -> Coord(it.first.x, y) }
                else if (it.first.y == it.second.y)
                    (it.first.x towards it.second.x).map { x -> Coord(x, it.first.y) }
                else
                    listOf()
            }
            .groupingBy { it }
            .eachCount()
            .count { it.value >= 2 }

        assertEquals(8111, overlaps)
    }

    private infix fun Int.towards(to: Int) =
        IntProgression.fromClosedRange(this, to, if (this > to) -1 else 1)
}