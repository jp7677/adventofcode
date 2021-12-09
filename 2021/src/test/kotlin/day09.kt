import kotlin.test.Test
import kotlin.test.assertEquals

class Day09 {
    data class Move (val x: Int, val y: Int)
    private val adjacents = listOf(Move(-1, 0), Move(1, 0), Move(0, -1), Move(0, 1))

    @Test
    fun `run part 01`() {
        val heatmap = Util.getInputAsListOfString("day09-input.txt")
            .map { it.map { c -> c.digitToInt() }.toTypedArray() }
            .toTypedArray()

        val riskLevel = heatmap.indices
            .sumOf { y ->
                heatmap.first().indices.sumOf { x ->
                    val current = heatmap[y][x]
                    if (adjacents.all {
                        heatmap.move(x, y, it) ?.let { adjacent -> adjacent > current } != false
                    })
                        current + 1
                    else
                        0
                }
            }

        assertEquals(600, riskLevel)
    }
}

private fun Array<Array<Int>>.move(x: Int, y: Int, step: Day09.Move) =
    if (x + step.x < 0 || x + step.x > this.first().count() - 1)
        null
    else if (y + step.y < 0 || y + step.y > this.count() - 1)
        null
    else
        this[y + step.y][x + step.x]