import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val value = IntCodeComputer(program.toTypedArray())
            .apply { noun = 12 }
            .apply { verb = 2 }
            .also { it.run() }
            .positionZero

        assertEquals (5866663, value)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfInt("day02-input.txt", ",")

        val nounVerb = (0..99)
            .crossJoin()
            .first {
                IntCodeComputer(program.toTypedArray())
                    .apply { noun = it.first }
                    .apply { verb = it.second }
                    .also { it.run() }
                    .positionZero == 19690720
            }
            .let { it.first * 100 + it.second }

        assertEquals (4259, nounVerb)
    }

    private fun IntRange.crossJoin() = flatMap { it1 -> map { it2 -> it1 to it2} }
}