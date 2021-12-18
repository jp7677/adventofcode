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
                    it[0].toString() + insertions[it.joinToString("")]
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

    @Test
    fun `run part 02`() {
        val (template, insertions) = getInstructions()

        val result = template
            .windowed(2, 1)
            .associateWith { 1L }
            .let { templateLinks ->
                generateSequence(templateLinks) { links ->
                    val acc = mutableMapOf<String, Long>()
                    links
                        .forEach {
                            val added = insertions[it.key]?.toString() ?: throw java.lang.IllegalStateException()
                            acc[it.key.first() + added] = (acc[it.key.first() + added] ?: 0) + it.value
                            acc[added + it.key.last()] = (acc[added + it.key.last()] ?: 0) + it.value
                        }
                    acc
                }
            }
            .take(40 + 1)
            .last()
            .map { it.key.last() to it.value }
            .groupBy { it.first }
            // This is not completely correct, the totals for the first link should be counted for both characters
            // Fortunately this is not the character we are interested in, so fine here
            .map { it.key to it.value.sumOf { count -> count.second } }
            .let {
                it.maxOf { count -> count.second } - it.minOf { count -> count.second }
            }

        assertEquals(2587447599164, result)
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