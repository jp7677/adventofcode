import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Coord14(var x: Int, var y: Int)

class Day14 : StringSpec({
    "puzzle part 01" {
        val rocks = getRocks()

        val countOfSand = rocks.flowSands().count() - rocks.count()

        countOfSand shouldBe 808
    }

    "puzzle part 02" {
        val rocks = getRocks()

        val minX = rocks.minOf { it.x }
        val maxX = rocks.maxOf { it.x }
        val distanceX = maxX - minX
        val maxY = rocks.maxOf { it.y }
        val bottom = ((minX - distanceX * 4)..(maxX + distanceX * 4))
            .map { Coord14(it, maxY + 2) }
            .toSet()

        val countOfSand = (rocks + bottom).flowSands()
            .count() - rocks.count() - bottom.count()

        countOfSand shouldBe 26625
    }
})

private fun Set<Coord14>.flowSands(): Set<Coord14> {
    val map = this.toMutableSet()
    while (true) {
        val sand = Coord14(500, 0)
        if (sand.isStuck(map)) return map + sand

        while (true) {
            if (!map.contains(sand.peekDown())) sand.moveDown()
            else if (!map.contains(sand.peekLeft())) sand.moveLeft()
            else if (!map.contains(sand.peekRight())) sand.moveRight()

            if (sand.flowsIntoAbyss(map)) return map
            if (sand.isStuck(map)) {
                map.add(sand)
                break
            }
        }
    }
}

private fun Coord14.peekDown() = Coord14(this.x, this.y + 1)
private fun Coord14.peekLeft() = Coord14(this.x - 1, this.y + 1)
private fun Coord14.peekRight() = Coord14(this.x + 1, this.y + 1)
private fun Coord14.moveDown() = this.y++
private fun Coord14.moveLeft() { this.x--; this.y++ }
private fun Coord14.moveRight() { this.x++; this.y++ }
private fun Coord14.flowsIntoAbyss(map: Set<Coord14>) = map.none { it.y > this.y }
private fun Coord14.isStuck(map: Set<Coord14>) = map
    .containsAll(listOf(this.peekDown(), this.peekLeft(), this.peekRight()))

private fun getRocks() = getPuzzleInput("day14-input.txt")
    .flatMap { line ->
        line.split(" -> ")
            .map { s ->
                s.split(",").let { Coord14(it.first().toInt(), it.last().toInt()) }
            }
            .fold(listOf<Coord14>()) { acc, it ->
                if (acc.any()) {
                    acc + (acc.last().x towards it.x).flatMap { x ->
                        (acc.last().y towards it.y).map { y -> Coord14(x, y) }
                    }
                } else listOf(it)
            }
    }.toSet()
