import kotlin.test.Test
import kotlin.test.assertEquals

class Day04 {

    @Test
    fun runPart01() {
        val count = countValidPasswords { digitWithNext ->
            digitWithNext.any { it.first == it.second } &&
                digitWithNext.all { it.first <= it.second }
        }

        assertEquals(979, count)
    }

    @Test
    fun runPart02() {
        val count = countValidPasswords { digitWithNext ->
            digitWithNext.any { it.first == it.second } &&
                digitWithNext.all { it.first <= it.second } &&
                digitWithNext
                    .filter { it.first == it.second }
                    .groupBy { it }
                    .any { it.value.size == 1 }
        }

        assertEquals(635, count)
    }

    private fun countValidPasswords(predicate: (List<Pair<Int, Int>>) -> Boolean) =
        Util.getInputAsString("day04-input.txt")
            .split('-')
            .map(String::toInt)
            .let { it.first()..it.last() }
            .count {
                predicate(
                    it
                        .digits()
                        .zipWithNext()
                )
            }

    private fun Int.digits() = toString().map { it.digitToInt() }
}
