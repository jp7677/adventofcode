import kotlin.test.Test
import kotlin.test.assertEquals

class Day13 {
    data class Coord(var x: Int, var y: Int)
    enum class Axis { X, Y}
    data class Instruction(val axis: Axis, val value: Int)

    @Test
    fun `run part 01`() {
        val (coords, instructions) = getPaper()

        coords.foldAxis(instructions.first())
        val count = coords
            .distinct()
            .count()

        assertEquals(781, count)
    }

    @Test
    fun `run part 02`() {
        val (coords, instructions) = getPaper()

        instructions.forEach { coords.foldAxis(it) }

        assertEquals("###..####.###...##...##....##.###..###.", coords.line(0))
        assertEquals("#..#.#....#..#.#..#.#..#....#.#..#.#..#", coords.line(1))
        assertEquals("#..#.###..#..#.#....#.......#.#..#.###.", coords.line(2))
        assertEquals("###..#....###..#....#.##....#.###..#..#", coords.line(3))
        assertEquals("#....#....#.#..#..#.#..#.#..#.#....#..#", coords.line(4))
        assertEquals("#....####.#..#..##...###..##..#....###.", coords.line(5))
    }

    private fun List<Coord>.foldAxis(instruction: Instruction) = this
        .forEach {
            if (instruction.axis == Axis.X) it.x = if (it.x < instruction.value) it.x else instruction.value - (it.x - instruction.value)
            if (instruction.axis == Axis.Y) it.y = if (it.y < instruction.value) it.y else instruction.value - (it.y - instruction.value)
        }

    private fun List<Coord>.line(index: Int) = (0..this.maxOf { it.x })
        .joinToString("") { x ->
            if (this.contains(Coord(x, index))) "#" else "."
        }

    private fun getPaper() = Util.getInputAsListOfString("day13-input.txt")
        .let { paper ->
            paper.mapNotNull {
                if (it.contains(","))
                    it
                        .split(",")
                        .let { s -> Coord(s[0].toInt(), s[1].toInt()) }
                else null
            } to
            paper.mapNotNull {
                if (it.startsWith("fold along"))
                    it
                        .split(" ", "=")
                        .let { s -> Instruction(Axis.valueOf(s[2].uppercase()), s[3].toInt()) }
                else null
            }
        }

    private fun List<Coord>.printCode() =
        (0..this.maxOf { it.y }).forEach { y ->
            (0..this.maxOf { it.x }).forEach { x ->
                print(if (this.contains(Coord(x, y))) "#" else ".")
            }
            println()
        }
}