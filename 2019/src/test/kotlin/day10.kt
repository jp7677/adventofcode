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
        (getPointsBetween(asteroid, other) intersect coords).isEmpty()

    private fun getPointsBetween(p: Pair<Int, Int>, q: Pair<Int, Int>) =
        if (q.first > p.first)
            plotRange(p.first + 1 until q.first, p, q)
        else if (q.first < p.first)
            plotRange(q.first + 1 until p.first, q, p)
        else
            if (p.second < q.second)
                (p.second + 1 until q.second).map { Pair(p.first, it) }
            else
                (q.second + 1 until p.second).map { Pair(p.first, it) }

    private fun plotRange(rangeX: Iterable<Int>, p: Pair<Int, Int>, q: Pair<Int, Int>) =
        rangeX
            .map { Pair(it, getY(it.toDouble(), p, q)) }
            .filter { it.second - it.second.toInt() == 0.0 }
            .map { Pair(it.first, it.second.toInt()) }

    private fun getY(x: Double, p: Pair<Int,Int>, q: Pair<Int,Int>) =
        p.second + (x - p.first) / (q.first - p.first) * (q.second - p.second)
}