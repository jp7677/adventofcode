import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {
    enum class Instruction { FORWARD, UP, DOWN }
    data class Command(val instruction: Instruction, val units: Int)
    data class Position(val position: Int, val depth: Int, val aim: Int)

    @Test
    fun `run part 01`() {
        val positionTimesDepth = getCourse()
            .groupingBy { it.instruction }
            .fold(0) { acc, it -> acc + it.units }
            .let { it.getValue(Instruction.FORWARD) * (it.getValue(Instruction.DOWN) - it.getValue(Instruction.UP)) }

        assertEquals(1635930, positionTimesDepth)
    }

    @Test
    fun `run part 02`() {
        val positionTimesDepth = getCourse()
            .fold(Position(0, 0, 0)) { acc, it ->
                Position(
                    if (it.instruction == Instruction.FORWARD) acc.position + it.units else acc.position,
                    if (it.instruction == Instruction.FORWARD) acc.depth + acc.aim * it.units else acc.depth,
                    when (it.instruction) {
                        Instruction.DOWN -> acc.aim + it.units
                        Instruction.UP -> acc.aim - it.units
                        else -> acc.aim
                    }
                )
            }
            .let { it.position * it.depth }

        assertEquals(1781819478, positionTimesDepth)
    }

    private fun getCourse() = Util.getInputAsListOfString("day02-input.txt")
        .map {
            it
                .split("\\s".toRegex())
                .let { s -> Command(Instruction.valueOf(s.first().uppercase()), s.last().toInt()) }
        }
}
