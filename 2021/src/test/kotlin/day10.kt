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
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> throw IllegalStateException()
                } as Int
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

    private fun String.getCorruptedClosingChar(): Char? {
        var line = this
        while (re.containsMatchIn(line)) {
            re.findAll(line)
                .firstNotNullOfOrNull {
                    line = line.replace(it.value, "")
                    if (!it.value.isValidPair())
                        it.value.last()
                    else
                        null
                }
                ?.let { return it }
        }

        return null
    }

    private fun String.isValidPair() = this.first().reversed() == this.last()

    private fun String.getMissingClosingChars(): String {
        var line = this
        while (re.containsMatchIn(line)) {
            line = re.replace(line, "")
        }

        return line
            .reversed()
            .map { it.reversed() }
            .joinToString("")
    }

    private fun Char.reversed() = when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalStateException()
    }
}