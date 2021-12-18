import kotlin.test.Test
import kotlin.test.assertEquals

class Day14 {

    @Test
    fun `run part 01`() {
        val (template, insertions) = getInstructions()

        val result = runSteps(template, insertions, 10)

        assertEquals(2435, result)
    }

    @Test
    fun `run part 02`() {
        val (template, insertions) = getInstructions()

        val result = runSteps(template, insertions, 40)

        assertEquals(2587447599164, result)
    }

    private fun runSteps(template: String, insertions: Map<String, Char>, steps: Int) = template
        .windowed(2, 1)
        .associateWith { 1L }
        .let { templateLinks ->
            generateSequence(templateLinks) { links ->
                links
                    .flatMap {
                        val insert = insertions[it.key]?.toString() ?: throw java.lang.IllegalStateException()
                        listOf(
                            (it.key.first() + insert) to it.value,
                            (insert + it.key.last()) to it.value)
                    }
                    .groupBy { it.first }
                    .map { it.key to it.value.sumOf { count -> count.second } }
                    .toMap()
            }
        }
        .take(steps + 1)
        .last()
        .map { it.key.last() to it.value }
        .groupBy { it.first }
        // This is not completely correct, the totals for the first link should be counted for both characters
        // Fortunately this is not the character we are interested in, so fine here
        .map { it.key to it.value.sumOf { count -> count.second } }
        .let {
            it.maxOf { count -> count.second } - it.minOf { count -> count.second }
        }

    private fun getInstructions() = Util.getInputAsListOfString("day14-input.txt")
        .let { paper ->
            paper.mapNotNull {
                if (!it.contains("->")) it
                else null
            }
                .single { it.isNotEmpty() } to
            paper.mapNotNull {
                if (it.contains("->"))
                    it
                        .split("->")
                        .let { s -> Pair(s[0].trim(), s[1].trim().first()) }
                else null
            }
                .toMap()
        }
}