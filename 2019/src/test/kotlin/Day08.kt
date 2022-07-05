import kotlin.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals

class Day08 {

    @Test
    fun runPart01() {
        val image = Util.getInputAsString("day08-input.txt")

        val number = image
            .chunked(25 * 6)
            .minByOrNull { it.count { c -> c == '0' } }
            .let { it ?: throw IllegalStateException() }
            .let { it.count { c -> c == '1' } * it.count { c -> c == '2' } }

        assertEquals(1215, number)
    }

    @Test
    fun runPart02() {
        val image = Util.getInputAsString("day08-input.txt")

        val decoded = image
            .chunked(25 * 6)
            .reduce { acc, layer ->
                acc.zip(layer) { a, b -> if (a == '2') b else a }
                    .toCharArray().concatToString()
            }
            .replaceMultiple("0" to " ", "1" to "#")
            .chunked(25)

        assertEquals("#    #  #  ##  ###  #  # ", decoded[0])
        assertEquals("#    #  # #  # #  # #  # ", decoded[1])
        assertEquals("#    #### #    #  # #### ", decoded[2])
        assertEquals("#    #  # #    ###  #  # ", decoded[3])
        assertEquals("#    #  # #  # #    #  # ", decoded[4])
        assertEquals("#### #  #  ##  #    #  # ", decoded[5])
    }
}
