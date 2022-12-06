import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day06 : StringSpec({
    "puzzle part 01" {
        val marker = getMarker(4)

        marker shouldBe 1702
    }

    "puzzle part 02" {
        val marker = getMarker(14)

        marker shouldBe 3559
    }
})

private fun getMarker(size: Int) = getPuzzleInput("day06-input.txt")
    .single().toList()
    .windowed(size).withIndex()
    .first { (_, window) -> window.distinct().size == window.size }
    .let { (index, window) -> index + window.size }
