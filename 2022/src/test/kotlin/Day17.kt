import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private data class Coord17(var x: Int, var y: Int)

private val shape1 = listOf(Coord17(2, -4), Coord17(3, -4), Coord17(4, -4), Coord17(5, -4))
private val shape2 = listOf(Coord17(3, -6), Coord17(2, -5), Coord17(3, -5), Coord17(4, -5), Coord17(3, -4))
private val shape3 = listOf(Coord17(4, -6), Coord17(4, -5), Coord17(2, -4), Coord17(3, -4), Coord17(4, -4))
private val shape4 = listOf(Coord17(2, -7), Coord17(2, -6), Coord17(2, -5), Coord17(2, -4))
private val shape5 = listOf(Coord17(2, -5), Coord17(3, -5), Coord17(2, -4), Coord17(3, -4))
private val shapes = listOf(shape1, shape2, shape3, shape4, shape5)

class Day17 : StringSpec({
    "puzzle part 01" {
        val jetPattern = getPuzzleInput("day17-input.txt").single().toList().toTypedArray()
        var pos = -1

        val map = buildSet { repeat(7) { add(Coord17(it, 0)) } }.toMutableList()
        var discarded = 0L
        repeat(2022) { round ->
            val offset = map.offsetY()
            if (offset > 0) map.forEach { it.y += offset }

            val shape = shapes[round % 5].map { it.copy() }
            while (true) {
                pos++
                when (jetPattern[pos % jetPattern.size]) {
                    '<' -> if (!shape.hitsLeftWall() && !shape.hitsLeft(map)) shape.forEach { it.x-- }
                    '>' -> if (!shape.hitsRightWall() && !shape.hitsRight(map)) shape.forEach { it.x++ }
                }

                if (shape.hitsBottom(map)) break
                shape.forEach { it.y++ }
            }
            map.addAll(shape)

            val lineY = map.groupBy { it.y }.filterValues { it.size == 7 }.keys.last()
            discarded += map.maxY() - lineY
            map.removeIf { it.y > lineY }
        }

        val height = map.minY().absoluteValue + map.maxY().absoluteValue + discarded

        height shouldBe 3065
    }
})

private fun List<Coord17>.hitsLeftWall() = any { it.x - 1 < 0 }
private fun List<Coord17>.hitsRightWall() = any { it.x + 1 > 6 }
private fun List<Coord17>.hitsBottom(map: MutableList<Coord17>) = any { map.contains(Coord17(it.x, it.y + 1)) }
private fun List<Coord17>.hitsLeft(map: MutableList<Coord17>) = any { map.contains(Coord17(it.x - 1, it.y)) }
private fun List<Coord17>.hitsRight(map: MutableList<Coord17>) = any { map.contains(Coord17(it.x + 1, it.y)) }

private fun List<Coord17>.offsetY() = 0 - this.minOf { it.y }
private fun List<Coord17>.minX() = this.minOf { it.x }
private fun List<Coord17>.maxX() = this.maxOf { it.x }
private fun List<Coord17>.minY() = this.minOf { it.y }
private fun List<Coord17>.maxY() = this.maxOf { it.y }

private fun List<Coord17>.debug() {
    (minY()..maxY()).forEach { y ->
        (minX()..maxX()).forEach { x -> if (this.any { it.x == x && it.y == y }) print("#") else print(".") }
        println()
    }
}
