import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val value = runProgram(program, 12, 2)
        assertEquals (5866663, value)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val nounVerb = (0..99)
            .crossJoin()
            .first { runProgram(program, it.first, it.second) == 19690720 }
            .let { it.first * 100 + it.second }

        assertEquals (4259, nounVerb)
    }

    private fun IntRange.crossJoin(): List<Pair<Int, Int>> =
        this.flatMap { it1 -> this.map { it2 -> it1 to it2} }

    private fun runProgram(program: List<Int>, noun: Int, verb: Int): Int {
        val memory = program.toTypedArray()
        memory[1] = noun
        memory[2] = verb

        memory
            .toList()
            .chunked(4)
            .takeWhile { it[0] != 99 }
            .forEach {
                val operation = if (it[0] == 1) add else multiply
                val input1 = memory[it[1]]
                val input2 = memory[it[2]]
                memory[it[3]] = operation(input1, input2)
            }

        return memory.first()
    }

    private val add: Int.(Int) -> Int = Int::plus
    private val multiply: Int.(Int) -> Int = Int::times
}