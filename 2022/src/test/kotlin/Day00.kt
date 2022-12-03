import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day00 : StringSpec({
    "puzzle part 01" {
        val input = getPuzzleInput("day00-input.txt")

        input.any() shouldBe true
    }
})
