import kotlin.test.Test
import kotlin.test.assertEquals

class Day14 {

    @Test
    fun `run part 01`() {
        val (template, insertions) = getInstructions()

        val result = generateSequence(template) { intermediateTemplate ->
            intermediateTemplate
                .toList()
                .windowed(2, 1)
                .joinToString("") {
                    it[0] + insertions.single { i -> i.first == it.joinToString("") }.second
                } + intermediateTemplate.last()
        }
            .take(10 + 1)
            .last()
            .groupingBy { it }
            .eachCount()
            .let { counts ->
                counts
                    .map { it.value }
                    .let {
                        (it.maxOrNull() ?: throw IllegalStateException()) -
                            (it.minOrNull() ?: throw IllegalStateException())
                    }

            }

        assertEquals(2435, result)
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
                        .let { s -> Pair(s[0].trim(), s[1].trim()) }
                else null
            }
        }
}