import kotlin.math.max
import kotlin.math.min
import kotlin.test.Ignore
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

    data class Universe(
        val player1Board: Board,
        val player2Board: Board,
        var player1Score: Int = 0,
        var player2Score: Int = 0
    ) {
        fun copy() = this.copy(
            player1Board = this.player1Board.copy(),
            player2Board = this.player2Board.copy()
        )
        fun playTurnForPlayer1(outcome: Int) {
            this.player1Score += this.player1Board.move(outcome)
        }
        fun playTurnForPlayer2(outcome: Int) {
            this.player2Score += this.player2Board.move(outcome)
        }
        fun running() = this.player1Score < 21 && this.player2Score < 21
        fun player1Wins() = this.player1Score > this.player2Score
    }

    @Test
    @Ignore("Needs way to much memory...")
    fun `run part 02`() {
        val (position1, position2) = getStartingPositions()

        val multiverse = mutableListOf(Universe(Board(position1), Board(position2)) to 1L)

        while (multiverse.any { (universe, _) -> universe.running() }) {
            multiverse
                .filter { (universe, _) -> universe.running() }
                .flatMapTo(multiverse) { (universe, occurrences) ->
                    diracDieOutcomes()
                        .map { (outcome, times) ->
                            universe.copy().apply { playTurnForPlayer1(outcome) } to times * occurrences
                        }
                        .flatMap { (universe1, occurrences1) ->
                            if (universe1.running())
                                diracDieOutcomes()
                                    .map { (outcome, times) ->
                                        universe1.copy().apply { playTurnForPlayer2(outcome) } to times * occurrences1
                                    }
                            else listOf(universe1 to occurrences1)
                        }
                        .drop(1)
                        .also {
                            universe.playTurnForPlayer1(diracDieOutcomes().first().first)
                            if (universe.running()) universe.playTurnForPlayer2(diracDieOutcomes().first().first)
                        }
                }
        }

        val maxWins = max(
            multiverse.filter { it.first.player1Wins() }.sumOf { it.second },
            multiverse.filter { !it.first.player1Wins() }.sumOf { it.second }
        )

        assertEquals(433315766324816, maxWins)
    }

    /*
        1                   2                   3
        1     2     3       1     2     3       1     2     3
        1 2 3 1 2 3 1 2 3   1 2 3 1 2 3 1 2 3   1 2 3 1 2 3 1 2 3

        'outcome' to 'number of occurrences'
    */
    private fun diracDieOutcomes() = listOf(
        3 to 1,
        4 to 3,
        5 to 6,
        6 to 7,
        7 to 6,
        8 to 3,
        9 to 1
    )

    private fun getStartingPositions() =
        Util.getInputAsListOfString("day21-input.txt")
            .map { it.split(':').last().trim().toInt() }
            .let { it.first() to it.last() }
}
