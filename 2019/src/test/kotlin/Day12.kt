import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12 {
    data class Vector(val x: Int, val y: Int, val z: Int) {
        operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)
        fun energy(): Int = abs(x) + abs(y) + abs(z)
    }

    data class Moon(val position: Vector, val velocity: Vector) {
        fun applyGravity(others: List<Moon>) = others
            .fold(velocity) { acc, moon ->
                Vector(
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

        fun applyVelocity(velocity: Vector) = position + velocity
    }

    @Test
    fun runPart01() {
        var moons = getMoons()

        repeat(1000) {
            moons = turn(moons)
        }

        val energy = moons.sumOf { it.position.energy() * it.velocity.energy() }

        assertEquals(9743, energy)
    }

    @Test
    fun runPart02() {
        var moons = getMoons()

        var rx = 0L
        var ry = 0L
        var rz = 0L

        var round = 0L
        while (rx == 0L || ry == 0L || rz == 0L) {
            moons = turn(moons).also { round++ }

            if (moons.sumOf { abs(it.velocity.x) } == 0 && rx == 0L)
                rx = round
            if (moons.sumOf { abs(it.velocity.y) } == 0 && ry == 0L)
                ry = round
            if (moons.sumOf { abs(it.velocity.z) } == 0 && rz == 0L)
                rz = round
        }

        val previousStateAt = 2 * rx.lcm(ry).lcm(rz)

        assertEquals(288684633706728, previousStateAt)
    }

    private fun turn(moons: List<Moon>) = moons
        .map { moon ->
            val velocity = moon.applyGravity(moons - moon)
            Moon(moon.applyVelocity(velocity), velocity)
        }

    private fun getMoons() = Util.getInputAsListOfString("day12-input.txt")
        .map {
            val (x, y, z) = re.findAll(it)
                .map { match -> match.value.toInt() }
                .toList()

            Moon(Vector(x, y, z), Vector(0, 0, 0))
        }

    private val re = "-?\\d+".toRegex()
}
