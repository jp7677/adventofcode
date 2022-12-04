import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Assignment(val start: Int, val end: Int) {
    infix fun contains(other: Assignment) = (start..end).let { other.start in it && other.end in it }
    infix fun overlaps(other: Assignment) = (start..end intersect other.start..other.end).any()
}

class Day04 : StringSpec({
    "puzzle part 01" {
        val count = getAssignments()
            .count { it.first contains it.second || it.second contains it.first }

        count shouldBe 509
    }

    "puzzle part 02" {
        val count = getAssignments()
            .count { it.first overlaps it.second }

        count shouldBe 870
    }
})

private fun getAssignments() = getPuzzleInput("day04-input.txt")
    .map { line ->
        line.split(',').map { slice ->
            slice.split("-").let { Assignment(it.first().toInt(), it.last().toInt()) }
        }
    }
    .map { it.first() to it.last() }
