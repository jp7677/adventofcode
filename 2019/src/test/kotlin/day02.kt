import kotlin.test.Test
import kotlin.test.junit5.JUnit5Asserter as Asserter

class Day02 {

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
                val operation: Int.(Int) -> Int = if (it[0] == 1) Int::plus else Int::times
                val input1 = program[it[1]]
                val input2 = program[it[2]]
                program[it[3]] = operation(input1, input2)
            }

        val result = program.first()
        Asserter.assertEquals("wrong result", 5866663, result)
    }
}