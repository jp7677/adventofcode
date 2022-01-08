import Util.towards
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22 {
    data class Coord(val x: Int, val y: Int, val z: Int)
    data class Cuboid(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val minZ: Int, val maxZ: Int)
    data class Step(val on: Boolean, val cuboid: Cuboid)

    @Test
    fun `run part 01`() {
        val steps = Util.getInputAsListOfString("day22-input.txt")
            .map {
                val s = it.split(' ')
                val r = s.last().split("=", "..", ",")
                Step(
                    s.first() == "on",
                    Cuboid(r[1].toInt(), r[2].toInt(), r[4].toInt(), r[5].toInt(), r[7].toInt(), r[8].toInt())
                )
            }

        val cubes = mutableSetOf<Coord>()
        steps
            .filter {
                it.cuboid.minX >= -50 && it.cuboid.maxX <= 50 &&
                it.cuboid.minY >= -50 && it.cuboid.maxY <= 50 &&
                it.cuboid.minZ >= -50 && it.cuboid.maxZ <= 50
            }
            .forEach {
                if (it.on)
                    cubes += it.cuboid.getCoords()
                else
                    cubes -= it.cuboid.getCoords()
            }

        assertEquals(653798, cubes.count())
    }

    private fun Cuboid.getCoords() =
        (this.minX towards this.maxX).flatMap { x ->
            (this.minY towards this.maxY).flatMap { y ->
                (this.minZ towards this.maxZ).map { z ->
                    Coord(x, y, z)
                }
            }
        }.toSet()
}