import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day02-input.txt", ",")
            .toTypedArray()

        val result = runProgram(memory, 1202)
        assertEquals (5866663, result)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfInt("day02-input.txt", ",")
            .toTypedArray()

        val result = (0..9999)
            .dropWhile { runProgram(memory.copyOf(), it) != 19690720 }
            .first()

        assertEquals (4259, result)
    }

    private val add: Int.(Int) -> Int = Int::plus
    private val multiply: Int.(Int) -> Int = Int::times

    private fun runProgram(memory: Array<Int>, nounVerb: Int): Int {
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
}