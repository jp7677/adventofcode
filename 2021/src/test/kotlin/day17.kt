import kotlin.test.Test
import kotlin.test.assertEquals

class Day17 {
    data class Coord(val x: Int, val y: Int)
    data class Trajectory(val position: Coord, val velocity: Coord)

    @Test
    fun `run part 01`() {
        val area = getArea()

        val maxY = (area.getStartX()..area.getEndX()).flatMap { x ->
            (-0..150).mapNotNull { y -> // TODO: Somehow calculate endY
                val trajectory = area.fireProbe(Coord(x, y))
                if (trajectory.first) trajectory.second.maxOf { it.position.y } else null
            }
        }
            .maxOrNull()

        assertEquals(6555, maxY)
    }

    private fun Set<Coord>.getStartX(): Int {
        var startX = 0
        var temp = 0
        while (temp < this.minX()) {
            temp += startX + 1
            startX++
        }
        return startX
    }

    private fun Set<Coord>.getEndX(): Int {
        var startX = 0
        var temp = 0
        while (temp < this.maxX()) {
            temp += startX + 1
            startX++
        }
        return startX
    }

    private fun Set<Coord>.fireProbe(initialVelocity: Coord): Pair<Boolean, Set<Trajectory>> {
        val areaMaxX = maxX()
        val areaMinY = minY()
        val steps = generateSequence(Trajectory(Coord(0, 0), initialVelocity)) {
            if (it.position.x > areaMaxX || it.position.y < areaMinY)
                null
            else {
                val position = Coord(it.position.x + it.velocity.x, it.position.y + it.velocity.y)
                val velocity = Coord(if (it.velocity.x == 0) 0 else it.velocity.x - 1, it.velocity.y - 1)
                Trajectory(position, velocity)
            }
        }.toSet()

        return (steps.map { it.position } intersect this).any() to steps
    }

    private fun Set<Coord>.maxX() = maxOf { it.x }
    private fun Set<Coord>.minX() = minOf { it.x }
    private fun Set<Coord>.minY() = minOf { it.y }

    private fun getArea() = Util.getInputAsString("day17-input.txt")
        .split("=", ".", ",")
        .mapNotNull { it.toIntOrNull() }
        .let { Coord(it[0], it[3]) to Coord(it[1], it[2]) }
        .let {
            (it.first.x..it.second.x).flatMap { x ->
                (it.second.y..it.first.y).map { y ->
                    Coord(x, y)
                }
            }
        }
        .sortedByDescending { it.y }
        .toSet()
}