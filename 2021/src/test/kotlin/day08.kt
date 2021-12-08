import kotlin.test.Test
import kotlin.test.assertEquals

class Day08 {
    data class Display(val digit: Int, val segments: Int)

    private val segments = arrayOf(
        Display(0, 6), Display(1, 2), Display(2, 5), Display(3, 5), Display(4, 4),
        Display(5, 5), Display(6, 6), Display(7, 3), Display(8, 7), Display(9, 6))

    @Test
    fun `run part 01`() {
        val uniqueDisplays = segments.filter { segments.count { s -> s.segments == it.segments } == 1 }

        val count = Util.getInputAsListOfString("day08-input.txt")
            .flatMap { it.split("|")[1].split(" ").toList() }
            .filter { segments -> uniqueDisplays.any { it.segments == segments.count() } }

        assertEquals(440, count.count())
    }
}