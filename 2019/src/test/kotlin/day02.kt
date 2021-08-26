import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {
    private val plus: Int.(Int) -> Int = Int::plus
    private val times: Int.(Int) -> Int = Int::times

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")
            .toTypedArray()

        program[1] = 12
        program[2] = 2
        program
            .toList()
            .chunked(4)
            .takeWhile { it[0] != 99 }
            .forEach {
                val operation = if (it[0] == 1) plus else times
                val input1 = program[it[1]]
                val input2 = program[it[2]]
                program[it[3]] = operation(input1, input2)
            }

        val result = program.first()
        assertEquals (5866663, result)
    }
}