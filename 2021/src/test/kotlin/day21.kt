import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21 {
    data class DeterministicDie(var rolls: Int = 0, var number: Int = 100) {
        fun roll(): Int {
            rolls++
            number = if (number == 100) 1 else number.inc()
            return number
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
        val start = Util.getInputAsListOfString("day21-input.txt")
            .map { it.split(':').last().trim().toInt() }
            .let { it.first() to it.last() }

        val die = DeterministicDie()
        val board1 = Board(start.first)
        var score1 = 0
        val board2 = Board(start.second)
        var score2 = 0

        while (score2 < 1000) {
            score1 += playTurn(die, board1)
            if (score1 >= 1000) break
            score2 += playTurn(die, board2)
        }

        val result = min(score1, score2) * die.rolls

        assertEquals(679329, result)
    }

    private fun playTurn(die: DeterministicDie, board1: Board) = generateSequence {
        die.roll()
    }
        .take(3)
        .sum()
        .let { board1.move(it) }
}