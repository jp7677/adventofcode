import kotlin.test.Test
import kotlin.test.assertEquals

class Day11 {
    data class Panel(val x: Int, val y: Int, var color: Long)
    enum class Direction{UP, RIGHT, DOWN, LEFT}

    @Test
    fun runPart01() {
        val program = Util.getInputAsListOfLong("day11-input.txt", ",").toLongArray()

        val result = getPanels(IntCodeComputer(program))
            .count()

        assertEquals(2041, result)
    }

    @Test
    fun runPart02() {
        val program = Util.getInputAsListOfLong("day11-input.txt", ",").toLongArray()

        val panels = getPanels(IntCodeComputer(program),1)
        val minX = panels.minOf { it.x }
        val maxX = panels.maxOf { it.x }

        val id = panels
            .filter { it.color == 1L }
            .groupBy { it.y }
            .toSortedMap()
            .map {
                val builder = StringBuilder()
                builder.append(" ".repeat(maxX - minX))
                it.value.forEach { panel -> builder[panel.x - minX] = '#' }
                builder.toString()
            }
            .reversed()

        assertEquals(" #### ###  #### ###  #  # #### #### ###   ", id[0])
        assertEquals("    # #  #    # #  # # #  #       # #  #  ", id[1])
        assertEquals("   #  #  #   #  #  # ##   ###    #  #  #  ", id[2])
        assertEquals("  #   ###   #   ###  # #  #     #   ###   ", id[3])
        assertEquals(" #    # #  #    #    # #  #    #    # #   ", id[4])
        assertEquals(" #### #  # #### #    #  # #### #### #  #  ", id[5])
    }

    private fun getPanels(robot: IntCodeComputer, initialPanelColor: Long = 0L) =
        mutableListOf(Panel(0, 0, initialPanelColor))
            .also {
                var direction = Direction.UP
                while (true) {
                    it.last().color = robot.runWithBreakOnOutput(it.last().color) ?: break
                    direction = turn(direction, robot.runWithBreakOnOutput() ?: throw IllegalStateException())

                    val nextX = getNextX(direction, it.last().x)
                    val nextY = getNextY(direction, it.last().y)
                    val nextColor = it.findLast { p -> p.x == nextX && p.y == nextY }?.color ?: 0
                    it.add(Panel(nextX, nextY, nextColor))
                }
            }
            .dropLast(1)
            .groupBy { Pair(it.x, it.y) }
            .map { it.value.last() }

    private fun turn(currentDirection: Direction, turn: Long) =
        when (currentDirection) {
            Direction.UP    -> if (turn == 0L) Direction.LEFT  else Direction.RIGHT
            Direction.RIGHT -> if (turn == 0L) Direction.UP    else Direction.DOWN
            Direction.DOWN  -> if (turn == 0L) Direction.RIGHT else Direction.LEFT
            Direction.LEFT  -> if (turn == 0L) Direction.DOWN  else Direction.UP
        }

    private fun getNextX(direction: Direction, currentX: Int) =
        when (direction) {
            Direction.RIGHT -> currentX + 1
            Direction.LEFT  -> currentX - 1
            Direction.UP,
            Direction.DOWN  -> currentX
        }

    private fun getNextY(direction: Direction, currentY: Int) =
        when (direction) {
            Direction.UP    -> currentY + 1
            Direction.DOWN  -> currentY - 1
            Direction.RIGHT,
            Direction.LEFT  -> currentY
        }
}