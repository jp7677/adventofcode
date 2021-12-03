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

    private fun List<String>.getPowerRating(type: RateType) =
        this
            .first()
            .mapIndexed { index, _ -> this.getBitFromIndex(index, type).toString() }
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
                val criteria = filtered.getBitFromIndex(index, type)
                filtered = filtered.filter { it[index] == criteria }
            }

        return filtered.toInt()
    }

    private fun List<String>.getBitFromIndex(index: Int, type: RateType): Char {
        val grouped = this
            .map { it[index] }
            .joinToString("")
            .groupingBy { it }
            .eachCount()
            .entries

        return if (type == RateType.GAMMA || type == RateType.OXYGEN)
            grouped
                .sortedByDescending { it.key }
                .maxByOrNull { it.value }
                ?.key ?: throw IllegalStateException()
        else
            grouped
                .sortedBy { it.key }
                .minByOrNull { it.value }
                ?.key ?: throw IllegalStateException()
    }

    private fun List<String>.toInt() = this
        .joinToString("")
        .toBigInteger(2)
        .toInt()
}