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

        var allXZeroAt = 0L
        var allYZeroAt = 0L
        var allZZeroAt = 0L
        var times = 0L
        while (allXZeroAt == 0L || allYZeroAt == 0L || allZZeroAt == 0L) {
            moons = turn(moons).also { times++ }

            if (allXZeroAt == 0L && moons.all { it.velocity.x == 0 })
                allXZeroAt = times
            if (allYZeroAt == 0L && moons.all { it.velocity.y == 0 })
                allYZeroAt = times
            if (allZZeroAt == 0L && moons.all { it.velocity.z == 0 })
                allZZeroAt = times
        }

        val previousStateAt = 2 * allXZeroAt.lcm(allYZeroAt).lcm(allZZeroAt)

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
