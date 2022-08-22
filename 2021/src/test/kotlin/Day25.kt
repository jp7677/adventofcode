import kotlin.test.Test
import kotlin.test.assertEquals

class Day25 {
    enum class Direction { EAST, SOUTH }

    data class Coord(val x: Int, val y: Int, val maxX: Int, val maxY: Int) {
        fun next(direction: Direction) = Coord(
            if (direction == Direction.EAST) {
                if (x.inc() < maxX) x.inc() else 0
            } else x,
            if (direction == Direction.SOUTH) {
                if (y.inc() < maxY) y.inc() else 0
            } else y,
            maxX,
            maxY
        )
    }

    @Test
    fun `run part 01`() {
        val map = Util.getInputAsListOfString("day25-input.txt")

        val cucumbers = map
            .flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, it ->
                    when (it) {
                        'v' -> Coord(x, y, line.length, map.size) to Direction.SOUTH
                        '>' -> Coord(x, y, line.length, map.size) to Direction.EAST
                        else -> null
                    }
                }
            }
            .toMap()

        val steps = generateSequence(cucumbers) {
            if (it.count { cucumber -> !it.containsKey(cucumber.key.next(cucumber.value)) } > 0)
                it
                    .move(Direction.EAST)
                    .move(Direction.SOUTH)
            else
                null
        }
            .count()

        assertEquals(429, steps)
    }

    private fun Map<Coord, Direction>.move(direction: Direction) = this
        .map {
            if (it.value == direction && !this.containsKey(it.key.next(it.value)))
                it.key.next(it.value) to it.value
            else
                it.key to it.value
        }
        .toMap()
}
