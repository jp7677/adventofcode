import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {
    private val re = "[(\\[{<][)\\]}>]".toRegex()

    @Test
    fun `run part 01`() {
        val lines = Util.getInputAsListOfString("day10-input.txt")

        val errorScore = lines
            .mapNotNull { it.getCorruptedClosingChar() }
            .sumOf {
                when (it) {
                    ')' -> 3L
                    ']' -> 57L
                    '}' -> 1197L
                    '>' -> 25137L
                    else -> throw IllegalStateException()
                }
            }

        assertEquals(265527, errorScore)
    }

    @Test
    fun `run part 02`() {
        val lines = Util.getInputAsListOfString("day10-input.txt")

        val middleScore = lines
            .filter { it.getCorruptedClosingChar() == null }
            .map { it.getMissingClosingChars() }
            .map { missing ->
                missing.fold(0.toLong()) { acc, it ->
                    when (it) {
                        ')' -> 1
                        ']' -> 2
                        '}' -> 3
                        '>' -> 4
                        else -> throw IllegalStateException()
                    } + acc * 5
                }
            }
            .sorted()
            .let { it[it.size / 2] }

        assertEquals(3969823589, middleScore)
    }

    private fun String.getCorruptedClosingChar(): Char? =
        generateSequence(this) {
            re.find(it)
                ?.let { match ->
                    if (match.value.isValidPair())
                        it.replace(match.value, "")
                    else
                        null
                }
        }
            .last()
            .let {
                re.find(it)
                    ?.let { match ->
                        if (match.value.last() in listOf(')', '}', ']', '>'))
                            match.value.last()
                        else
                            null
                    }
            }

    private fun String.isValidPair() = this.first().toClosingChar() == this.last()

    private fun String.getMissingClosingChars() =
        generateSequence(this) {
            if (re.containsMatchIn(it)) re.replace(it, "") else null
        }
            .last()
            .reversed()
            .map { it.toClosingChar() }
            .joinToString("")

    private fun Char.toClosingChar() = when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalStateException()
    }
}
