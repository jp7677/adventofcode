import kotlin.test.Test
import kotlin.test.assertEquals

class Day03 {
    enum class RateType { GAMMA, EPSILON, OXYGEN, CO2 }

    @Test
    fun `run part 01`() {
        val report = Util.getInputAsListOfString("day03-input.txt")

        val powerConsumption = report
            .let { it.getPowerRating(RateType.GAMMA) * it.getPowerRating(RateType.EPSILON) }

        assertEquals(4103154, powerConsumption)
    }

    private fun List<String>.getPowerRating(type: RateType) = this
        .first()
        .mapIndexed { index, _ -> this.getCommonBit(index, type).toString() }
        .toInt()

    @Test
    fun `run part 02`() {
        val report = Util.getInputAsListOfString("day03-input.txt")

        val lifeSupport = report
            .let { it.getLifeSupportRating(RateType.OXYGEN) * it.getLifeSupportRating(RateType.CO2) }

        assertEquals(4245351, lifeSupport)
    }

    private fun List<String>.getLifeSupportRating(type: RateType): Int {
        var filtered = this.toList()

        this
            .first()
            .forEachIndexed { index, _ ->
                val bit = filtered.getCommonBit(index, type)
                filtered = filtered.filter { it[index] == bit }
            }

        return filtered.toInt()
    }

    private fun List<String>.getCommonBit(index: Int, type: RateType) = this
        .map { it[index].digitToInt() }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedBy { if (type == RateType.EPSILON || type == RateType.CO2) it.key else it.key.unaryMinus() }
        .maxByOrNull { if (type == RateType.GAMMA || type == RateType.OXYGEN) it.value else it.value.unaryMinus() }
        ?.key?.digitToChar() ?: throw IllegalStateException()

    private fun List<String>.toInt() = this
        .joinToString("")
        .toBigInteger(2)
        .toInt()
}
