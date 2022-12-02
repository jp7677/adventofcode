import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

enum class Shape { ROCK, PAPER, SCISSORS;
    companion object {
        fun from(input: String): Shape =
            when(input) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException(input)
            }
        }

    fun wonOf(you: Shape): Boolean =
        if (this == ROCK && you == SCISSORS) true
        else if (this == SCISSORS && you == PAPER) true
        else if (this == PAPER && you == ROCK) true
        else false
}

data class Game(val other: Shape, val you: Shape) {
    fun scoreForSelected() = when (you) {
        Shape.ROCK -> 1
        Shape.PAPER -> 2
        Shape.SCISSORS -> 3
    }

    fun scoreForOutcome() =
        if (other == you) 3
        else if (you.wonOf(other)) 6
        else if (other.wonOf(you)) 0
        else throw IllegalStateException()
}

class Day02 : StringSpec({

    "puzzle part 01" {
        val totalScore = Util.getInputAsListOfString("day02-input.txt")
            .map { it.split(' ') }
            .map { Game(Shape.from(it.first()), Shape.from(it.last())) }
            .sumOf { it.scoreForOutcome() + it.scoreForSelected() }

        totalScore shouldBe 14531
    }
})
