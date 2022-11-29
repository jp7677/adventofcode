import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day00 : StringSpec({

    "run part 01" {
        val input = Util.getInputAsListOfString("day00-input.txt")

        input.any() shouldBe true
    }
})
