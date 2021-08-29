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

    private fun Int.digits() : List<Int> = this.toString()
        .map { it.toString().toInt() }
}