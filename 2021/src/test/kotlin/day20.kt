import kotlin.test.Test
import kotlin.test.assertEquals

class Day20 {
    data class Pixel(val x: Int, val y: Int)
    private val processingInput = listOf(
        Pixel(-1, -1), Pixel(0, -1), Pixel(1, -1),
        Pixel(-1, 0), Pixel(0, 0), Pixel(1, 0),
        Pixel(-1, 1), Pixel(0, 1), Pixel(1, 1),
    )

    @Test
    fun `run part 01`() {
        val (algorithm, image) = Util.getInputAsListOfString("day20-input.txt")
            .let {
                it.first().map { c -> c == '#' } to it
                    .drop(2)
                    .let { image ->
                        image.first()
                            .indices
                            .flatMap { x ->
                                List(image.size) { y -> if (image[y][x] == '#') Pixel(x, y) else null }
                            }
                    }
                    .mapNotNull { pixel -> pixel }
                    .toSet()
            }

        val processed = generateSequence(image) {
            val processed = mutableSetOf<Pixel>()
            (it.minY()..it.maxY()).forEach { y ->
                (it.minX()..it.maxX()).forEach { x ->
                    val index = it.getIndex(x ,y)
                    if (algorithm[index]) processed.add(Pixel(x, y))
                }
            }
            processed
        }
            .take(2 + 1)
            .last()
            .let {
                val minX = it.minX()
                val maxX = it.maxX()
                val minY = it.minY()
                val maxY = it.maxY()

                it.filterNot { pixel ->
                    pixel.x in (minX..(minX+10)) ||
                    pixel.x in ((maxX-10)..maxX) ||
                    pixel.y in (minY..(minY+10)) ||
                    pixel.y in ((maxY-10)..maxY)
                }.toSet()
            }

        val lightPixelCount = processed.count()

        assertEquals(5498, lightPixelCount)
    }

    private fun Set<Pixel>.getIndex(x: Int, y: Int) = processingInput.map {
        if (this.contains(Pixel(x + it.x, y + it.y))) '1' else '0'
    }
        .joinToString("")
        .let {
            Integer.parseInt(it, 2)
        }

    private fun Set<Pixel>.debug() =
        (this.minY()..this.maxY()).forEach { y ->
            (this.minX()..this.maxX()).forEach { x ->
                if (this.contains(Pixel(x, y))) print('#') else print('.')
            }
            println()
        }

    private fun Set<Pixel>.minX() = this.minOf { it.x - 5 }
    private fun Set<Pixel>.maxX() = this.maxOf { it.x + 5 }
    private fun Set<Pixel>.minY() = this.minOf { it.y - 5 }
    private fun Set<Pixel>.maxY() = this.maxOf { it.y + 5 }
}