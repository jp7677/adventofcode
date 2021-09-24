import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {

    @Test
    fun runPart01() {
        val map = Util.getInputAsListOfString("day10-input.txt")

        val coords = map
            .flatMapIndexed { indexY, line -> line.mapIndexed { indexX, char -> Triple(indexX, indexY, char) } }
            .filter { it.third == '#' }
            .map { Pair(it.first, it.second) }

        val detected = coords
            .maxOf {
                coords
                    .filter { c -> c != it }
                    .count{ other -> isVisible(it, other, coords) }
            }

        assertEquals(274, detected)
    }

    private fun isVisible(asteroid: Pair<Int, Int>, other: Pair<Int, Int>, coords: List<Pair<Int, Int>>) =
        (positionsBetween(asteroid, other) intersect coords).isEmpty()

    private fun positionsBetween(p: Pair<Int, Int>, q: Pair<Int, Int>) =
        if (q.first != p.first)
            (p.first towards q.first)
                .trim(1)
                .map { Pair(it, getY(it.toDouble(), p, q)) }
                .filter { it.second.isWholeNumber() }
                .map { Pair(it.first, it.second.toInt()) }
        else
            (p.second towards q.second)
                .trim(1)
                .map { Pair(p.first, it) }

    private fun getY(x: Double, p: Pair<Int,Int>, q: Pair<Int,Int>) =
        p.second + (x - p.first) / (q.first - p.first) * (q.second - p.second)
}