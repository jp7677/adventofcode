import io.kotest.matchers.shouldBe
import kotlin.test.Test

class Day00 {

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day00-input.txt")

        input.any() shouldBe true
    }
}
