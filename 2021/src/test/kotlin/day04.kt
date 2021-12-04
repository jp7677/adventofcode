import kotlin.test.Test
import kotlin.test.assertEquals

class Day04 {
    data class Board constructor (val rows: List<List<Int>>, val cols: List<List<Int>>) {
        constructor(rows: List<List<Int>>) :
        this(
            rows,
            List(rows.first().size) { index -> rows.map { it[index] } }
        )

        val all get() = rows.flatten()
    }

    @Test
    fun `run part 01`() {
        val (boards, numbers) = getGame()

        val winningBoard = playGame(boards, numbers).first()
        val score = calcScore(winningBoard, numbers)

        assertEquals(55770, score)
    }

    @Test
    fun `run part 02`() {
        val (boards, numbers) = getGame()

        val winningBoard = playGame(boards, numbers).last()
        val score = calcScore(winningBoard, numbers)

        assertEquals(2980, score)
    }

    private fun getGame(): Pair<List<Board>, List<Int>> {
        val bingoSubSystem = Util.getInputAsListOfString("day04-input.txt")

        val numbers = bingoSubSystem.first()
            .split(",")
            .map { Integer.parseInt(it) }
        val size = bingoSubSystem[2].toRow().size
        val boards = bingoSubSystem
            .drop(1)
            .filter { it.isNotBlank() }
            .chunked(size)
            .map { Board(it.map { s -> s.toRow() }) }

        return Pair(boards, numbers)
    }

    private fun playGame(boards: List<Board>, numbers: List<Int>) = numbers.indices
        .flatMap { draw ->
            numbers
                .take(draw)
                .let { drawnNumbers ->
                    boards
                        .filter {
                            it.rows.any { r -> drawnNumbers.containsAll(r) }
                                || it.cols.any { c -> drawnNumbers.containsAll(c) }
                        }
                        .map { Pair(it, drawnNumbers.last()) }
                }
        }
        .distinctBy { it.first }

    private fun calcScore(winningBoard: Pair<Board, Int>, numbers: List<Int>) = winningBoard.first
        .all
        .filterNot { numbers.takeUntil(winningBoard.second).contains(it) }
        .sum() * winningBoard.second

    private fun List<Int>.takeUntil(last: Int): List<Int> = this
        .takeWhile { it != last } + last

    private fun String.toRow() = this
        .split("\\s".toRegex())
        .filter { it.isNotBlank() }
        .map { Integer.parseInt(it) }
}