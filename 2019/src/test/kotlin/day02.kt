import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val result = runProgram(program, 1202)
        assertEquals (5866663, result)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val result = (0..9999)
            .first { runProgram(program.toList(), it) == 19690720 }

        assertEquals (4259, result)
    }

    private fun runProgram(program: List<Int>, nounVerb: Int): Int {
        val memory = program.toTypedArray()
        memory[1] = (nounVerb - nounVerb % 100) / 100
        memory[2] = nounVerb % 100

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