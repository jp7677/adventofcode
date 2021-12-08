import kotlin.test.Test
import kotlin.test.assertEquals

class Day08 {
    data class Note(val patterns: List<String>, val outputs: List<String>)
    data class Display(val digit: Int, val segments: String)

    private val displays = arrayOf(
        Display(0, "abcefg".sorted()),
        Display(1, "cf".sorted()),
        Display(2, "acdeg".sorted()),
        Display(3, "acdfg".sorted()),
        Display(4, "bcdf".sorted()),
        Display(5, "abdfg".sorted()),
        Display(6, "abdefg".sorted()),
        Display(7, "acf".sorted()),
        Display(8, "abcdefg".sorted()),
        Display(9, "abcdfg".sorted()))

    private val displaysWithUniqueSegmentCount = displays
        .filter { displays.count { s -> s.segments.length == it.segments.length } == 1 }

    @Test
    fun `run part 01`() {
        val count = getNotes()
            .flatMap { it.outputs }
            .count { output ->
                displaysWithUniqueSegmentCount.any { it.segments.length == output.length }
            }

        assertEquals(440, count)
    }

    @Test
    fun `run part 02`() {
        val count = getNotes()
            .sumOf { note ->
                val mapping = mutableMapOf(
                    Pair("a", "abcdefg"),
                    Pair("b", "abcdefg"),
                    Pair("c", "abcdefg"),
                    Pair("d", "abcdefg"),
                    Pair("e", "abcdefg"),
                    Pair("f", "abcdefg"),
                    Pair("g", "abcdefg")
                )

                mapping.deduceBySegmentCountOverAllDigits(note.patterns)
                mapping.deduceBySegmentCountPerDigit(note.patterns)
                note.outputs.decode(mapping)
            }

        assertEquals(1046281, count)
    }

    private fun MutableMap<String, String>.deduceBySegmentCountOverAllDigits(patterns: List<String>) {
        val segmentsWithUniqueCountOverAllDigits = listOf("b", "e", "f")
        val otherSegments = listOf("a", "c", "d", "g")

        segmentsWithUniqueCountOverAllDigits
            .onEach { segment ->
                val count = displays.joinToString { it.segments }.count { s -> s.toString() == segment}
                this[segment] = patterns.joinToString("")
                    .groupBy { it }
                    .filter { it.value.count() == count }
                    .keys.single()
                    .toString()
            }

        this
            .filterKeys { it in segmentsWithUniqueCountOverAllDigits }
            .onEach {
                otherSegments.onEach { segment ->
                    this[segment] = this[segment]!!.replace(it.value, "")
                }
            }
    }

    private fun MutableMap<String, String>.deduceBySegmentCountPerDigit(pattern: List<String>) = pattern
        .filter { p -> displaysWithUniqueSegmentCount.any { it.segments.length == p.length } }
        .onEach {
            displays
                .filter { s -> s.segments.length == it.length }
                .map { it.segments }
                .single()
                .let { segments ->
                    this.onEach { r ->
                        if (segments.contains(r.key))
                            this[r.key] = (this[r.key]!!.toSet() intersect it.toSet()).joinToString("")
                        else
                            this[r.key] = (this[r.key]!!.toSet() - it.toSet()).joinToString("")
                    }
                }
        }

    private fun List<String>.decode(mapping: MutableMap<String, String>) = this
        .map {
            it
                .map { c -> mapping.keys.single { k -> mapping[k] == c.toString() } }
                .let { translated ->
                    displays.single { d -> d.segments == translated.joinToString("").sorted() }
                }
        }
        .joinToString("") { it.digit.toString() }.toInt()

    private fun getNotes() = Util.getInputAsListOfString("day08-input.txt")
        .map {
            it
                .split("|")
                .let { parts ->
                    Note(
                        parts.first().split(" ").filter { s -> s.isNotEmpty() }.map { s -> s.sorted() }.toList(),
                        parts.last().split(" ").filter { s -> s.isNotEmpty() }.map { s -> s }.toList()
                    )
                }
        }

    private fun String.sorted() = this.toList().sorted().joinToString("")
}