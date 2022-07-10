import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21 {
    class DeterministicDie {
        var rolls: Int = 0
            private set

        private var outcome: Int = 0

        fun roll(): Int {
            outcome = if (outcome == 100) 1 else outcome.inc()
            return outcome.also { rolls++ }
        }
    }

    data class Board(var space: Int) {
        fun move(times: Int): Int {
            space = if (space + times > 10) (space + times) % 10 else space + times
            if (space == 0) space = 10
            return space
        }
    }

    @Test
    fun `run part 01`() {
        val (position1, position2) = getStartingPositions()

        val die = DeterministicDie()
        val (score1, score2) = playDeterministicGame(die, Board(position1), Board(position2))

        val result = min(score1, score2) * die.rolls

        assertEquals(679329, result)
    }

    private fun playDeterministicGame(
        die: DeterministicDie,
        board1: Board,
        board2: Board
    ): Pair<Int, Int> {
        var score1 = 0
        var score2 = 0
        while (true) {
            score1 += playTurn(die, board1)
            if (score1 >= 1000) break

            score2 += playTurn(die, board2)
            if (score2 >= 1000) break
        }

        return score1 to score2
    }

    private fun playTurn(die: DeterministicDie, board1: Board) = generateSequence {
        die.roll()
    }
        .take(3)
        .sum()
        .let { board1.move(it) }

    private fun getStartingPositions() =
        Util.getInputAsListOfString("day21-input.txt")
            .map { it.split(':').last().trim().toInt() }
            .let { it.first() to it.last() }
}
