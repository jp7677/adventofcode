import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Unit(var x: Int, var y: Int)

class Day14 : StringSpec({
    "puzzle part 01" {
        val rocks = getRocks()

        val countOfSand = rocks.produceSand() - rocks.count()

        countOfSand shouldBe 808
    }

    "puzzle part 02" {
        val rocks = getRocks()
        val minX = rocks.minOf { it.x }
        val maxX = rocks.maxOf { it.x }
        val distanceX = maxX - minX
        val maxY = rocks.maxOf { it.y }
        val bottom = ((minX - distanceX * 4)..(maxX + distanceX * 4))
            .map { Unit(it, maxY + 2) }
            .toSet()

        val countOfSand = (rocks + bottom).produceSand() - rocks.count() - bottom.count()

        countOfSand shouldBe 26625
    }
})

private fun Set<Unit>.produceSand(): Int {
    val map = this.toMutableSet()
    while (true) {
        val sand = Unit(500, 0)
        if (sand.cameToRest(map)) return map.size + 1

        while (true) {
            if (!map.contains(sand.peekDown())) sand.moveDown()
            else if (!map.contains(sand.peekLeft())) sand.moveLeft()
            else if (!map.contains(sand.peekRight())) sand.moveRight()

            if (sand.flowsIntoAbyss(map)) return map.size
            if (sand.cameToRest(map)) break
        }
        map.add(sand)
    }
}

private fun Unit.peekDown() = Unit(x, y + 1)
private fun Unit.peekLeft() = Unit(x - 1, y + 1)
private fun Unit.peekRight() = Unit(x + 1, y + 1)
private fun Unit.moveDown() = this.y++
private fun Unit.moveLeft() { this.x--; this.y++ }
private fun Unit.moveRight() { this.x++; this.y++ }
private fun Unit.flowsIntoAbyss(map: Set<Unit>) = map.none { it.y > this.y }
private fun Unit.cameToRest(map: Set<Unit>) = map
    .containsAll(listOf(this.peekDown(), this.peekLeft(), this.peekRight()))

private fun getRocks() = getPuzzleInput("day14-input.txt")
    .flatMap { line ->
        line.split(" -> ").map { s ->
            s.split(",").let { Unit(it.first().toInt(), it.last().toInt()) }
        }
            .fold(listOf<Unit>()) { acc, it ->
                if (acc.any()) {
                    acc + (acc.last().x towards it.x).flatMap { x ->
                        (acc.last().y towards it.y).map { y -> Unit(x, y) }
                    }
                } else listOf(it)
            }
    }.toSet()
