import kotlin.test.Test
import kotlin.test.assertEquals

class Day04 {
    data class Board constructor (val rows: List<List<Int>>, val cols: List<List<Int>>) {
        constructor(rows: List<List<Int>>) :
        this(
            rows,
            List(rows.first().size) { index -> rows.map { it[index] } }
        )

        val numbers get() = rows.flatten()
    }

    @Test
    fun `run part 01`() {
        val (size, numbers, boards) = getGame()

        val finishedBoards = playGame(size, numbers, boards)
        val score = calcScore(finishedBoards.first(), numbers)

        assertEquals(55770, score)
    }

    @Test
    fun `run part 02`() {
        val (size, numbers, boards) = getGame()

        val finishedBoards = playGame(size, numbers, boards)
        val score = calcScore(finishedBoards.last(), numbers)

        assertEquals(2980, score)
    }

    private fun getGame(): Triple<Int, List<Int>, List<Board>> {
        val bingoSubSystem = Util.getInputAsListOfString("day04-input.txt")

        val size = bingoSubSystem[4].toRow().size
        val numbers = bingoSubSystem.first().toNumbers()
        val boards = bingoSubSystem
            .drop(1)
            .filter { it.isNotBlank() }
            .chunked(size)
            .map { Board(it.map { s -> s.toRow() }) }

        return Triple(size, numbers, boards)
    }

    private fun playGame(size: Int, numbers: List<Int>, boards: List<Board>): MutableList<Pair<Board, Int>> {
        val finishedBoards: MutableList<Pair<Board, Int>> = mutableListOf()

        (size..numbers.size)
            .onEach { draw ->
                val drawnNumbers = numbers.take(draw)
                boards
                    .filter {
                        it.rows.any { r -> drawnNumbers.containsAll(r) }
                        || it.cols.any { r -> drawnNumbers.containsAll(r) }
                    }
                    .onEach {
                        if (finishedBoards.none { f -> f.first == it })
                            finishedBoards.add(Pair(it, drawnNumbers.last()))
                    }
            }

        return finishedBoards
    }

    private fun calcScore(winningBoard: Pair<Board, Int>, numbers: List<Int>): Int {
        val drawnNumbers = numbers.takeWhile { it != winningBoard.second } + winningBoard.second
        return winningBoard.first.numbers.filterNot { drawnNumbers.contains(it) }.sum() * winningBoard.second
    }

    private fun String.toNumbers() = this
        .split(",")
        .map { Integer.parseInt(it) }

    private fun String.toRow() = this
        .split("\\s".toRegex())
        .filter { it.isNotBlank() }
        .map { Integer.parseInt(it) }
}