import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day10-input.txt")

        val errorScore = input
            .mapNotNull { it.getFirstCorrupted() }
            .sumOf {
                when (it) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> throw IllegalStateException()
                } as Int
            }

        assertEquals(265527, errorScore)
    }

    private val re = "[(\\[{<][)\\]}>]".toRegex()

    private fun String.getFirstCorrupted(): Char? {
        var line = this
        while (re.containsMatchIn(line)) {
            re.findAll(line)
                .firstNotNullOfOrNull {
                    line = line.replace(it.value, "")
                    if (!it.value.isMatchingClosingChar())
                        it.value.last()
                    else
                        null
                }
                ?.let { return it }
        }

        return null
    }

    private fun String.isMatchingClosingChar() =
        this.first().code in (this.last().code - 2) until this.last().code
}