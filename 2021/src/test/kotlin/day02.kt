import kotlin.test.Test
import kotlin.test.assertEquals

class Day02 {

    @Test
    fun `run part 01`() {
        val course = Util.getInputAsListOfString("day02-input.txt")
            .map { it.split(' ') }
            .map { it[0] to it[1].toInt() }

        val aggr = course
            .groupingBy { it.first }
            .fold(0) { acc, element -> acc + element.second }

        val result = aggr.getValue("forward") * (aggr.getValue("down") - aggr.getValue("up"))

        assertEquals(1635930, result)
    }
}