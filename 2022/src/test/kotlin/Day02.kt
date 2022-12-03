import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private enum class Outcome {
    WIN, LOOSE, DRAW;

    companion object {
        fun from(input: String) =
            when (input) {
                "X" -> LOOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw IllegalArgumentException(input)
            }
    }

    fun score() = when (this) {
        WIN -> 6
        LOOSE -> 0
        DRAW -> 3
    }
}

private enum class Shape {
    ROCK, PAPER, SCISSORS;

    companion object {
        fun from(input: String) =
            when (input) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException(input)
            }
    }

    fun score() = when (this) {
        ROCK -> 1
        PAPER -> 2
        SCISSORS -> 3
    }
}

private data class Game(val other: Shape, val you: Shape?, val outcome: Outcome?) {
    fun scoreForSelected() = you?.score() ?: throw IllegalStateException()
    fun scoreForOutcome() = outcome?.score() ?: throw IllegalStateException()

    fun calcOutcome() = Game(
        other,
        you,
        when (you) {
            other.forWin() -> Outcome.WIN
            other.forLoose() -> Outcome.LOOSE
            else -> Outcome.DRAW
        }
    )

    fun decrypt() = Game(
        other,
        when (outcome) {
            Outcome.DRAW -> other
            Outcome.WIN -> other.forWin()
            Outcome.LOOSE -> other.forLoose()
            else -> throw IllegalStateException()
        },
        outcome
    )

    private fun Shape.forLoose() = this.forWin().forWin()

    private fun Shape.forWin() =
        when (this) {
            Shape.ROCK -> Shape.PAPER
            Shape.PAPER -> Shape.SCISSORS
            Shape.SCISSORS -> Shape.ROCK
        }
}

class Day02 : StringSpec({
    "puzzle part 01" {
        val totalScore = Util.getInputAsListOfString("day02-input.txt")
            .map { it.split(' ') }
            .map { Game(Shape.from(it.first()), Shape.from(it.last()), null) }
            .map { it.calcOutcome() }
            .sumOf { it.scoreForOutcome() + it.scoreForSelected() }

        totalScore shouldBe 14531
    }

    "puzzle part 02" {
        val totalScore = Util.getInputAsListOfString("day02-input.txt")
            .map { it.split(' ') }
            .map { Game(Shape.from(it.first()), null, Outcome.from(it.last())) }
            .map { it.decrypt() }
            .sumOf { it.scoreForOutcome() + it.scoreForSelected() }

        totalScore shouldBe 11258
    }
})
