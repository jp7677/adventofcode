import kotlin.test.Test
import kotlin.test.assertEquals

class Day11 {
    data class Panel(val x: Int, val y: Int, var color: Long)
    enum class Direction{UP, RIGHT, DOWN, LEFT}

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day11-input.txt", ",").toLongArray()
        val brain = IntCodeComputer(program)

        var direction = Direction.UP
        val painted = mutableListOf(Panel(0, 0, 0))
        while (true) {
            painted.last().color = brain.runWithBreakOnOutput(painted.last().color) ?: break
            val turn = brain.runWithBreakOnOutput() ?: throw IllegalStateException()

            direction = when(direction) {
                Direction.UP -> if (turn == 0L) Direction.LEFT else Direction.RIGHT
                Direction.RIGHT -> if (turn == 0L) Direction.UP else Direction.DOWN
                Direction.DOWN -> if (turn == 0L) Direction.RIGHT else Direction.LEFT
                Direction.LEFT -> if (turn == 0L) Direction.DOWN else Direction.UP
            }

            val nextX = when(direction) {
                Direction.RIGHT -> painted.last().x + 1
                Direction.LEFT -> painted.last().x - 1
                Direction.UP,
                Direction.DOWN -> painted.last().x
            }

            val nextY = when(direction) {
                Direction.UP -> painted.last().y + 1
                Direction.DOWN -> painted.last().y - 1
                Direction.RIGHT,
                Direction.LEFT -> painted.last().y
            }

            val nextColor = painted.findLast { p -> p.x == nextX && p.y == nextY }
                ?.color
                ?: 0

            painted.add(Panel(nextX, nextY, nextColor))
        }

        val result = painted
            .dropLast(1)
            .groupBy { Pair(it.x, it.y) }
            .count()

        assertEquals(2041, result)
    }
}