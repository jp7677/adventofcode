import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
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
}