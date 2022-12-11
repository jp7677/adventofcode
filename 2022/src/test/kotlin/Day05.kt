import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Move(val times: Int, val src: Int, val dest: Int)

class Day05 : StringSpec({
    "puzzle part 01" {
        val (stacks, moves) = getStacksAndMoves()

        moves.forEach { move ->
            repeat(move.times) {
                stacks[move.dest].add(stacks[move.src].removeLast())
            }
        }

        val result = stacks.joinToString("") { it.removeLast() }

        result shouldBe "TWSGQHNHL"
    }

    "puzzle part 02" {
        val (stacks, moves) = getStacksAndMoves()
        val deque = ArrayDeque<String>(1)

        moves.forEach { move ->
            repeat(move.times) {
                deque.add(stacks[move.src].removeLast())
            }
            repeat(move.times) {
                stacks[move.dest].add(deque.removeLast())
            }
            deque.clear()
        }

        val result = stacks.joinToString("") { it.removeLast() }

        result shouldBe "JNRSCDWPP"
    }
})

private fun getStacksAndMoves(): Pair<List<ArrayDeque<String>>, List<Move>> {
    val input = getPuzzleInput("day05-input.txt", "$eol$eol").toList()

    val startingStacks = input.first().split(eol)
    val numberOfStacks = startingStacks.last().trim().last().digitToInt()
    val stacks = buildList<ArrayDeque<String>> {
        repeat(numberOfStacks) { add(ArrayDeque(1)) }
    }

    startingStacks.reversed().drop(1).forEach {
        for (i in 1..numberOfStacks * 4 step 4)
            if (it.length >= i && it[i].isLetter())
                stacks[(i - 1) / 4].add(it[i].toString())
    }

    val moves = input.last().split(eol)
        .map { line ->
            line.split(" ").let { Move(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }
        }

    return stacks to moves
}
