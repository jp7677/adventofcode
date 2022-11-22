import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class Day00 {

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day00-input.txt")

        assertThat(input.any()).isEqualTo(true)
    }
}
