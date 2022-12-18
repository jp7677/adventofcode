import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private data class Cube(val x: Int, val y: Int, val z: Int)
private val adjacent = listOf(
    Cube(1, 0, 0),
    Cube(-1, 0, 0),
    Cube(0, 1, 0),
    Cube(0, -1, 0),
    Cube(0, 0, 1),
    Cube(0, 0, -1)
)

private data class Cocoon(val lava: Set<Cube>) {
    val minX = lava.minOf { it.x } - 1
    val maxX = lava.maxOf { it.x } + 1
    val minY = lava.minOf { it.y } - 1
    val maxY = lava.maxOf { it.y } + 1
    val minZ = lava.minOf { it.z } - 1
    val maxZ = lava.maxOf { it.z } + 1
    val sizeX = (maxX - minX + 1).absoluteValue
    val sizeY = (maxY - minY + 1).absoluteValue
    val sizeZ = (maxZ - minZ + 1).absoluteValue
    val cubes: Set<Cube>

    init {
        cubes = Cube(minX, minY, minZ).findAllAdjacent()
    }

    private fun Cube.findAllAdjacent(visited: MutableSet<Cube> = mutableSetOf()): Set<Cube> =
        setOf(this) + adjacent
            .map { n -> Cube(x + n.x, y + n.y, z + n.z) }
            .filterNot {
                it.x < minX || it.x > maxX || it.y < minY || it.y > maxY || it.z < minZ || it.z > maxZ ||
                    lava.contains(it) || visited.contains(it)
            }
            .onEach { visited.add(it) }
            .flatMap { it.findAllAdjacent(visited) }

    fun innerSurfaceSize() = cubes.exposedSides() - outerSurfaceSize()
    private fun outerSurfaceSize() = 2 * (sizeX * sizeY + sizeX * sizeZ + sizeY * sizeZ)
}

class Day18 : StringSpec({
    "puzzle part 01" { getCubes().exposedSides() shouldBe 3542 }
    "puzzle part 02" { Cocoon(getCubes()).innerSurfaceSize() shouldBe 2080 }
})

private fun Set<Cube>.exposedSides() = sumOf {
    6 - adjacent.count { n -> contains(Cube(it.x + n.x, it.y + n.y, it.z + n.z)) }
}

private fun getCubes() = getPuzzleInput("day18-input.txt")
    .map { it.split(',').let { s -> Cube(s[0].toInt(), s[1].toInt(), s[2].toInt()) } }
    .toSet()
