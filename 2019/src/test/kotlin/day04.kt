import kotlin.test.Test
import kotlin.test.assertEquals

class Day04 {

    @Test
    fun runPart01() {
        val count = Util.getInputAsString("day04-input.txt")
            .split('-')
            .map(String::toInt)
            .let { it.first()..it.last() }
            .count { password ->
                val digitWithNext = password
                    .digits()
                    .zipWithNext()

                digitWithNext.any { it.first == it.second }
                    && digitWithNext.all { it.first <= it.second }
        }

        assertEquals(979, count)
    }

    @Test
    fun runPart02() {
        val count = Util.getInputAsString("day04-input.txt")
            .split('-')
            .map(String::toInt)
            .let { it.first()..it.last() }
            .count { password ->
                val digitWithNext = password
                    .digits()
                    .zipWithNext()

                digitWithNext.any { it.first == it.second }
                    && digitWithNext.all { it.first <= it.second }
                    && digitWithNext
                        .filter { it.first == it.second }
                        .groupBy { it }
                        .any { it.value.size == 1 }
            }

        assertEquals(635, count)
    }

    private fun Int.digits() : List<Int> = this.toString()
        .map { it.toString().toInt() }
}