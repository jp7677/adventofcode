import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private data class Position(val x: Int, val y: Int)

class Day09 : StringSpec({
    "puzzle part 01" {
        val countOfPositions = getHeadPositions()
            .runningReduce { acc, head -> acc.follow(head) }
            .toSet().count()

        countOfPositions shouldBe 5619
    }

    "puzzle part 02" {
        val knots = buildList { repeat(9) { add(listOf(Position(0, 0))) } }
        val countOfPositions = getHeadPositions()
            .fold(knots) { rope, head ->
                rope
                    .map { it.last() }
                    .scan(head) { knots, it -> it.follow(knots) }
                    .drop(1)
                    .mapIndexed { index, it ->
                        if (index == rope.size - 1) rope[index] + it else listOf(it)
                    }
            }
            .last()
            .toSet().count()

        countOfPositions shouldBe 2376
    }
})

private fun getHeadPositions() = getPuzzleInput("day09-input.txt")
    .map { it.split(" ") }
    .map { it.first() to it.last().toInt() }
    .flatMap { (move, times) ->
        buildList { repeat(times) { add(move) } }
    }
    .scan(Position(0, 0)) { acc, it -> acc.move(it) }

private fun Position.move(direction: String) = when (direction) {
    "R" -> Position(this.x + 1, this.y)
    "L" -> Position(this.x - 1, this.y)
    "U" -> Position(this.x, this.y + 1)
    "D" -> Position(this.x, this.y - 1)
    else -> throw IllegalArgumentException()
}

private fun Position.follow(other: Position) =
    if ((this.x - other.x).absoluteValue <= 1 && (this.y - other.y).absoluteValue <= 1)
        this
    else when {
        other.x > x && other.y == y -> Position(x + 1, y)
        other.x < x && other.y == y -> Position(x - 1, y)
        other.x == x && other.y > y -> Position(x, y + 1)
        other.x == x && other.y < y -> Position(x, y - 1)
        other.x > x && other.y > y -> Position(x + 1, y + 1)
        other.x < x && other.y > y -> Position(x - 1, y + 1)
        other.x > x -> Position(x + 1, y - 1)
        other.x < x -> Position(x - 1, y - 1)
        else -> throw IllegalArgumentException()
    }
