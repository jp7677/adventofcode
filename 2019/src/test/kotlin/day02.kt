import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day02-input.txt", ",")

        val positionZero = IntCodeComputer(program.toLongArray())
            .apply { noun = 12; verb = 2 }
            .also { it.runToCompletion() }
            .positionZero

        assertEquals (5866663, positionZero)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day02-input.txt", ",")

        val nounVerb = (0L..99L)
            .crossJoin()
            .first {
                IntCodeComputer(program.toLongArray())
                    .apply { noun = it.first; verb = it.second }
                    .also { it.runToCompletion() }
                    .positionZero == 19690720L
            }
            .let { it.first * 100 + it.second }

        assertEquals (4259, nounVerb)
    }

    private fun LongRange.crossJoin() = flatMap { it1 -> map { it2 -> it1 to it2} }
}