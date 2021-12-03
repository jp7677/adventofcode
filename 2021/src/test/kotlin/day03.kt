import kotlin.test.Test
import kotlin.test.assertEquals

class Day03 {
    enum class RateType { GAMMA, EPSILON}

    @Test
    fun `run part 01`() {
        val report = Util.getInputAsListOfString("day03-input.txt")

        val bitsList = List(report.first().length) { index ->
            report
                .map { it[index] }
                .joinToString("")
        }

        val powerConsumption = bitsList.getPowerRating(RateType.GAMMA) * bitsList.getPowerRating(RateType.EPSILON)

        assertEquals(4103154, powerConsumption)
    }

    private fun List<String>.getPowerRating(type: RateType) = this
        .map { bit ->
            val map = bit
                .groupingBy { it }
                .eachCount()

            if (type == RateType.GAMMA)
                map.maxByOrNull { it.value }?.key
            else
                map.minByOrNull { it.value }?.key
        }
        .joinToString("")
        .toBigInteger(2)
        .toInt()
}