import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day03 : StringSpec({
    "puzzle part 01" {
        val sumOfPriorities = Util.getInputAsListOfString("day03-input.txt")
            .map { it.duplicated() }
            .sumOf { it.priority() }

        sumOfPriorities shouldBe 7446
    }

    "puzzle part 02" {
        val sumOfPriorities = Util.getInputAsListOfString("day03-input.txt")
            .map { rucksack -> rucksack.filterNot { it == rucksack.duplicated() } }
            .chunked(3)
            .map { it[0].toSet() intersect it[1].toSet() intersect it[2].toSet() }
            .sumOf { it.single().priority() }

        sumOfPriorities shouldBe 2646
    }
})

private fun String.duplicated() = this
    .map { chunked(length / 2) }
    .map { it[0].toSet() intersect it[1].toSet() }
    .first().single()

private fun Char.priority() = if (isLowerCase())
    code - 97 + 1
else
    code - 65 + 27
