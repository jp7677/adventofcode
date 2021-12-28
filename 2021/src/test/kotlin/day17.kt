import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17 {
    data class Coord(val x: Int, val y: Int)
    data class Step(val position: Coord, val velocity: Coord)

    @Test
    fun `run part 01`() {
        val area = getArea()

        val maxY = (area.getMinInitialVelocityX()..area.getMaxInitialVelocityX()).flatMap { velocityX ->
            (area.minY()..abs(area.minY())).mapNotNull { velocityY ->
                area.fireProbe(Coord(velocityX, velocityY)).let { (hit, steps) ->
                    if (hit) steps.maxOf { step -> step.position.y } else null
                }
            }
        }
            .maxOrNull()

        assertEquals(6555, maxY)
    }

    @Test
    fun `run part 02`() {
        val area = getArea()

        val maxY = (area.getMinInitialVelocityX()..area.maxX()).flatMap { x ->
            (area.minY()..abs(area.minY())).mapNotNull { y ->
                area.fireProbe(Coord(x, y)).let { (hit, _) -> hit }
            }
        }
            .count { it }

        assertEquals(4973, maxY)
    }

    private fun Set<Coord>.getMinInitialVelocityX() = generateSequence(1 to 1) { (index, it) ->
        if (index + it < this.minX()) index.inc() to index + it else null
    }
        .count()

    private fun Set<Coord>.getMaxInitialVelocityX() = generateSequence(1 to 1) { (index, it) ->
        if (index + it < this.maxX()) index.inc() to index + it else null
    }
        .count()

    private fun Set<Coord>.fireProbe(initialVelocity: Coord): Pair<Boolean, Set<Step>> {
        val areaMaxX = maxX()
        val areaMinY = minY()
        val steps = generateSequence(Step(Coord(0, 0), initialVelocity)) {
            if (it.position.x <= areaMaxX && it.position.y >= areaMinY)
                Step(
                    Coord(it.position.x + it.velocity.x, it.position.y + it.velocity.y),
                    Coord(if (it.velocity.x == 0) 0 else it.velocity.x - 1, it.velocity.y - 1)
                )
            else
                null
        }
            .toSet()

        return (steps.map { it.position } intersect this).any() to steps
    }

    private fun Set<Coord>.minX() = minOf { it.x }
    private fun Set<Coord>.maxX() = maxOf { it.x }
    private fun Set<Coord>.minY() = minOf { it.y }

    private fun getArea() = Util.getInputAsString("day17-input.txt")
        .split("=", ".", ",")
        .mapNotNull { it.toIntOrNull() }
        .let { Coord(it[0], it[3]) to Coord(it[1], it[2]) }
        .let { (topLeft, bottomRight) ->
            (topLeft.x..bottomRight.x).flatMap { x ->
                (bottomRight.y..topLeft.y).map { y ->
                    Coord(x, y)
                }
            }
        }
        .toSet()
}