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
        val bingoSubSystem = Util.getInputAsListOfString("day04-input.txt")

        val size = bingoSubSystem[4].toRow().size
        val numbers = bingoSubSystem.first().toNumbers()
        val boards = bingoSubSystem
            .drop(1)
            .filter { it.isNotBlank() }
            .chunked(size)
            .map { Board(it.map { s -> s.toRow() }) }

        var drawnNumbers: List<Int> = listOf()
        var winningBoard: Board? = null

        (size..numbers.size)
            .first { draw ->
                drawnNumbers = numbers.take(draw)
                boards
                    .filter {
                        it.rows.any { r -> drawnNumbers.containsAll(r) }
                        || it.cols.any { r -> drawnNumbers.containsAll(r) }
                    }
                    .onEach { winningBoard = it }
                    .isNotEmpty()
            }

        val score = winningBoard
            ?.let { b -> b.numbers.filterNot { drawnNumbers.contains(it) }.sum() * drawnNumbers.last() }

        assertEquals(55770, score)
    }

    private fun String.toNumbers() = this
        .split(",")
        .map { Integer.parseInt(it) }

    private fun String.toRow() = this
        .split("\\s".toRegex())
        .filter { it.isNotBlank() }
        .map { Integer.parseInt(it) }
}