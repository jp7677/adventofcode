import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private enum class Op { NOOP, ADDX }
private data class Cycle(val during: Int, val after: Int)

class Day10 : StringSpec({
    "puzzle part 01" {
        val cycles = getCycles()
        val signalStrength = listOf(20, 60, 100, 140, 180, 220).sumOf {
            cycles[it - 1] * it
        }

        signalStrength shouldBe 14920
    }

    "puzzle part 02" {
        val screen = getCycles()
            .chunked(40)
            .map {
                it.mapIndexed { centerOfSprite, x ->
                    if (x in (centerOfSprite - 1..centerOfSprite + 1)) '#' else '.'
                }.joinToString("")
            }

        screen[0] shouldBe "###..#..#..##...##...##..###..#..#.####."
        screen[1] shouldBe "#..#.#..#.#..#.#..#.#..#.#..#.#..#....#."
        screen[2] shouldBe "###..#..#.#....#..#.#....###..#..#...#.."
        screen[3] shouldBe "#..#.#..#.#....####.#....#..#.#..#..#..."
        screen[4] shouldBe "#..#.#..#.#..#.#..#.#..#.#..#.#..#.#...."
        screen[5] shouldBe "###...##...##..#..#..##..###...##..####."
    }
})

private fun getCycles() = getPuzzleInput("day10-input.txt")
    .map { it.split(" ") }
    .map {
        when (it.first()) {
            "noop" -> Op.valueOf(it.first().uppercase()) to 0
            "addx" -> Op.valueOf(it.first().uppercase()) to it.last().toInt()
            else -> throw IllegalArgumentException()
        }
    }
    .fold(listOf(Cycle(1, 1))) { acc, it ->
        val x = acc.last().after
        acc + when (it.first) {
            Op.NOOP -> listOf(Cycle(x, x))
            Op.ADDX -> listOf(Cycle(x, x), Cycle(x, x + it.second))
        }
    }
    .drop(1)
    .map { it.during }
