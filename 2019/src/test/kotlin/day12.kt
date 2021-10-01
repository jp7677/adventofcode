import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12 {
    data class Position(val x: Int, val y: Int, val z: Int) {
        fun pot(): Int = abs(x) + abs(y) + abs(z)
    }
    data class Velocity(val x: Int, val y: Int, val z: Int) {
        fun kin(): Int = abs(x) + abs(y) + abs(z)
    }
    data class Moon(val position: Position, val velocity: Velocity) {
        fun applyGravity(others: List<Moon>) =
            others.fold(velocity) { acc, moon ->
                Velocity(
                    calcAxis(position.x, moon.position.x, acc.x),
                    calcAxis(position.y, moon.position.y, acc.y),
                    calcAxis(position.z, moon.position.z, acc.z)
                )
            }

        private fun calcAxis(axis: Int, otherAxis: Int, axisVelocity: Int) =
            if (axis < otherAxis)
                axisVelocity + 1
            else if (axis > otherAxis)
                axisVelocity - 1
            else
                axisVelocity

        fun applyVelocity(velocity: Velocity) = Position(position.x + velocity.x, position.y + velocity.y, position.z + velocity.z)
    }

    @Test
    fun runPart01() {
        val re = "-?\\d+".toRegex()
        var moons = Util.getInputAsListOfString("day12-input.txt")
            .map {
                val (x,y,z) = re.findAll(it)
                    .map { match ->  match.value.toInt() }
                    .toList()

                Moon(Position(x, y, z), Velocity(0, 0, 0))
            }

        repeat(1000) {
            moons = moons.map {
                val velocity = moons
                    .filter { moon -> it != moon }
                    .let { moons -> it.applyGravity(moons) }

                Moon(it.applyVelocity(velocity), velocity)
            }
        }

        val energy = moons.sumOf { it.position.pot() * it.velocity.kin() }

        assertEquals(9743, energy)
    }
}