import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private data class Cube(val x: Int, val y: Int, val z: Int)
private val neighbours = listOf(
    Cube(1, 0, 0),
    Cube(-1, 0, 0),
    Cube(0, 1, 0),
    Cube(0, -1, 0),
    Cube(0, 0, 1),
    Cube(0, 0, -1)
)

private data class Cocon(val lava: Set<Cube>) {
    val minX = lava.minOf { it.x } - 1
    val maxX = lava.maxOf { it.x } + 1
    val minY = lava.minOf { it.y } - 1
    val maxY = lava.maxOf { it.y } + 1
    val minZ = lava.minOf { it.z } - 1
    val maxZ = lava.maxOf { it.z } + 1

    fun sideX() = (maxX - minX + 1).absoluteValue
    fun sideY() = (maxY - minY + 1).absoluteValue
    fun sideZ() = (maxZ - minZ + 1).absoluteValue
    fun outerSize() = (2 * sideX() * sideY()) + (2 * sideX() * sideZ()) + (2 * sideY() * sideZ())

    fun buildCocon() = Cube(minX, minY, minZ).getNeighbours()

    private fun Cube.getNeighbours(visited: MutableSet<Cube> = mutableSetOf()): Set<Cube> {
        val potentialNeighbours = neighbours
            .map { n -> Cube(x + n.x, y + n.y, z + n.z) }
            .filterNot { it.x < minX || it.x > maxX || it.y < minY || it.y > maxY || it.z < minZ || it.z > maxZ }
            .filterNot { lava.contains(it) }
            .filterNot { visited.contains(it) }

        return setOf(this) + potentialNeighbours
            .onEach { visited.add(it) }
            .flatMap {
                it.getNeighbours(visited)
            }
    }
}

class Day18 : StringSpec({
    "puzzle part 01" {
        val cubes = getCubes()

        val sides = cubes.sidesCount()

        sides shouldBe 3542
    }

    "puzzle part 02" {
        val cubes = getCubes()

        val cocon = Cocon(cubes)
        val sides = cocon.buildCocon().sidesCount() - cocon.outerSize()

        sides shouldBe 2080
    }
})

private fun Set<Cube>.sidesCount() = sumOf {
    6 - neighbours.count { n ->
        contains(Cube(it.x + n.x, it.y + n.y, it.z + n.z))
    }
}

private fun getCubes() = getPuzzleInput("day18-input.txt")
    .map { it.split(',').let { s -> Cube(s[0].toInt(), s[1].toInt(), s[2].toInt()) } }
    .toSet()
