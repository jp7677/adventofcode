import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day10-input.txt")

        val errorScore = input
            .mapNotNull { it.getCorrupted() }
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
        val input = Util.getInputAsListOfString("day10-input.txt")

        val middleScore = input
            .filter { it.getCorrupted() == null }
            .map { it.getIncomplete() }
            .map { incompletes ->
                incompletes.fold(0.toLong()) { acc, it ->
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

    private val re = "[(\\[{<][)\\]}>]".toRegex()

    private fun String.getCorrupted(): Char? {
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

    private fun String.getIncomplete(): String {
        var line = this
        while (re.containsMatchIn(line)) {
            re.findAll(line)
                .toList()
                .onEach { line = line.replace(it.value, "") }
        }

        return line
            .reversed()
            .map {
                when (it) {
                    '(' -> ')'
                    '[' -> ']'
                    '{' -> '}'
                    '<' -> '>'
                    else -> throw IllegalStateException()
                }
            }
            .joinToString("")
    }
}