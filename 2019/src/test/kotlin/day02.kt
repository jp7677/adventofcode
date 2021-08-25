import kotlin.test.Test
import kotlin.test.junit5.JUnit5Asserter as Asserter

class Day02 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")
            .toTypedArray()

        program[1] = 12
        program[2] = 2
        for (i in 0..program.size step 4) {
            if (program[i] == 99)
                break

            val operator: Int.(Int) -> Int = if (program[i] == 1) Int::plus else Int::times
            val input1 = program[program[i + 1]]
            val input2 = program[program[i + 2]]
            program[program[i + 3]] = operator(input1, input2)
        }

        val result = program.first()
        Asserter.assertEquals("wrong result", 5866663, result)
    }
}