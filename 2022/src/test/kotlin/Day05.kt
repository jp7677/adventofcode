import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Move(val num: Int, val src: Int, val dest: Int)

class Day05 : StringSpec({
    "puzzle part 01" {
        val (stacks, moves) = getCratesAndMoves()

        moves.forEach { move ->
            repeat(move.num) {
                stacks[move.dest].add(stacks[move.src].removeLast())
            }
        }

        val result = stacks.joinToString("") { it.removeLast() }

        result shouldBe "TWSGQHNHL"
    }

    "puzzle part 02" {
        val (stacks, moves) = getCratesAndMoves()

        moves.forEach { move ->
            val q = ArrayDeque<String>(move.num)
            repeat(move.num) {
                q.add(stacks[move.src].removeLast())
            }
            repeat(move.num) {
                stacks[move.dest].add(q.removeLast())
            }

        }

        val result = stacks.joinToString("") { it.removeLast() }

        result shouldBe "JNRSCDWPP"
    }
})

private fun getCratesAndMoves(): Pair<List<ArrayDeque<String>>, List<Move>> {
    val input = getPuzzleInput("day05-input.txt", "${eol()}${eol()}").toList()

    val stacksPlan = input[0].split(eol()).reversed()
    val numberOfStacks = stacksPlan.first().trim().last().toString().toInt()
    val stacks = buildList<ArrayDeque<String>> {
        repeat(numberOfStacks) { add(ArrayDeque(1)) }
    }

    stacksPlan.drop(1).forEach {
        for (i in 1..numberOfStacks * 4 step 4) {
            if (it.length >= i && it[i].isLetter()) {
                stacks[(i - 1) / 4].add(it[i].toString())
            }
        }
    }

    val moves = input[1].split(eol())
        .map { it.split(" ").let { s -> Move(s[1].toInt(), s[3].toInt() - 1, s[5].toInt() - 1) } }

    return stacks to moves
}
