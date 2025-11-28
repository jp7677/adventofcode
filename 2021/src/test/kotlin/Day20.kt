import kotlin.test.Test
import kotlin.test.assertEquals

class Day20 {
    data class Pixel(val x: Int, val y: Int)

    @Suppress("ktlint:standard:argument-list-wrapping")
    private val processingInput = listOf(
        Pixel(-1, -1), Pixel(0, -1), Pixel(1, -1),
        Pixel(-1, 0), Pixel(0, 0), Pixel(1, 0),
        Pixel(-1, 1), Pixel(0, 1), Pixel(1, 1)
    )

    @Test
    fun `run part 01`() {
        val (algorithm, image) = getProcessingData()

        val count = image.processImage(algorithm, 2)
            .count()

        assertEquals(5498, count)
    }

    @Test
    fun `run part 02`() {
        val (algorithm, image) = getProcessingData()

        val count = image.processImage(algorithm, 50)
            .count()

        assertEquals(16014, count)
    }

    private fun Set<Pixel>.processImage(algorithm: Array<Boolean>, rounds: Int) =
        generateSequence(this to false) { (it, isLit) ->
            val minX = it.minX()
            val maxX = it.maxX()
            val minY = it.minY()
            val maxY = it.maxY()

            val processed = mutableSetOf<Pixel>()
            (it.minY().dec()..it.maxY().inc()).forEach { y ->
                (it.minX().dec()..it.maxX().inc()).forEach { x ->
                    if (algorithm[it.getIndex(x, y, minX, maxX, minY, maxY, isLit)])
                        processed.add(Pixel(x, y))
                }
            }
            processed to (if (isLit) algorithm.last() else algorithm.first())
        }
            .map { it.first }
            .take(rounds.inc())
            .last()

    private fun Set<Pixel>.getIndex(x: Int, y: Int, minX: Int, maxX: Int, minY: Int, maxY: Int, isLit: Boolean) =
        processingInput
            .map {
                if (((x + it.x) in minX..maxX) && ((y + it.y) in minY..maxY)) {
                    if (this.contains(Pixel(x + it.x, y + it.y)))
                        '1'
                    else
                        '0'
                } else if (isLit)
                    '1'
                else
                    '0'
            }
            .joinToString("")
            .let {
                Integer.parseInt(it, 2)
            }

    private fun Set<Pixel>.minX() = this.minOf { it.x }
    private fun Set<Pixel>.maxX() = this.maxOf { it.x }
    private fun Set<Pixel>.minY() = this.minOf { it.y }
    private fun Set<Pixel>.maxY() = this.maxOf { it.y }

    private fun getProcessingData() = Util.getInputAsListOfString("day20-input.txt")
        .let {
            it.first().map { c -> c == '#' }.toTypedArray() to it
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
}
