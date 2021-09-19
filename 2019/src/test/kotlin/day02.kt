import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfLong("day02-input.txt", ",")

        val positionZero = IntCodeComputer(memory.toLongArray())
            .apply { noun = 12; verb = 2 }
            .also { it.runUntilExit() }
            .positionZero

        assertEquals (5866663, positionZero)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfLong("day02-input.txt", ",")

        val nounVerb = (0L..99L)
            .crossJoin()
            .first {
                IntCodeComputer(memory.toLongArray())
                    .apply { noun = it.first; verb = it.second }
                    .also { it.runUntilExit() }
                    .positionZero == 19690720L
            }
            .let { it.first * 100 + it.second }

        assertEquals (4259, nounVerb)
    }

    private fun LongRange.crossJoin() = flatMap { it1 -> map { it2 -> it1 to it2} }
}