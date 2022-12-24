import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private sealed interface Coord22 {
    val x: Int
    val y: Int
}

private data class Tile(override val x: Int, override val y: Int) : Coord22
private data class Wall(override val x: Int, override val y: Int) : Coord22

private enum class Direction { UP, RIGHT, DOWN, LEFT }
private data class Position22(var coord: Coord22, var direction: Direction)

private enum class Turn { CLOCKWISE, COUNTERCLOCKWISE }
private sealed interface PathSegment
private data class Move22(val steps: Int) : PathSegment
private data class PathTurn(val turn: Turn) : PathSegment

class Day22 : StringSpec({
    "puzzle part 01" {
        val (map, path) = getMapAndPath()
        val pos = Position22(Tile(map.filter { it.y == 0 }.minOf { it.x }, 0), Direction.RIGHT)

        path.forEach { segment ->
            when (segment) {
                is PathTurn -> {
                    pos.direction = pos.direction.turn(segment.turn)
                }
                is Move22 -> repeat(segment.steps) {
                    map.peek(pos)?.let {
                        pos.coord = it
                    } ?: return@repeat
                }
            }
        }

        map.debug(pos)

        val password = ((pos.coord.y + 1) * 1000) + ((pos.coord.x + 1) * 4) + when (pos.direction) {
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
            Direction.LEFT -> 2
            Direction.UP -> 3
        }

        password shouldBe 123046
    }
})

private fun Direction.turn(turn: Turn) = when (turn) {
    Turn.CLOCKWISE -> when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }

    Turn.COUNTERCLOCKWISE -> when (this) {
        Direction.UP -> Direction.LEFT
        Direction.RIGHT -> Direction.UP
        Direction.DOWN -> Direction.RIGHT
        Direction.LEFT -> Direction.DOWN
    }
}

private fun Set<Coord22>.peek(pos: Position22): Coord22? {
    val next = when (pos.direction) {
        Direction.UP -> Tile(pos.coord.x, pos.coord.y.dec())
            .let { if (containsCoord(it.x, it.y)) it else Tile(it.x, this.filter { m -> m.x == it.x }.maxOf { m -> m.y }) }
        Direction.RIGHT -> Tile(pos.coord.x.inc(), pos.coord.y)
            .let { if (containsCoord(it.x, it.y)) it else Tile(this.filter { m -> m.y == it.y }.minOf { m -> m.x }, it.y) }
        Direction.DOWN -> Tile(pos.coord.x, pos.coord.y.inc())
            .let { if (containsCoord(it.x, it.y)) it else Tile(it.x, this.filter { m -> m.x == it.x }.minOf { m -> m.y }) }
        Direction.LEFT -> Tile(pos.coord.x.dec(), pos.coord.y)
            .let { if (containsCoord(it.x, it.y)) it else Tile(this.filter { m -> m.y == it.y }.maxOf { m -> m.x }, it.y) }
    }

    return if (this.contains(next)) next else null
}

private fun Set<Coord22>.containsCoord(x: Int, y: Int) = contains(Tile(x, y)) || contains(Wall(x, y))

private fun Set<Coord22>.debug(pos: Position22) {
    val minX = minOf { it.x }
    val maxX = maxOf { it.x }
    val minY = minOf { it.y }
    val maxY = maxOf { it.y }

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            if (any { pos.coord.x == x && pos.coord.y == y }) when (pos.direction) {
                Direction.UP -> print("^")
                Direction.RIGHT -> print(">")
                Direction.DOWN -> print("v")
                Direction.LEFT -> print("<")
            }
            else when (singleOrNull { it.x == x && it.y == y }) {
                is Tile -> print(".")
                is Wall -> print("#")
                null -> print(" ")
            }
        }
        println()
    }
}

private fun getMapAndPath() = getPuzzleInput("day22-input.txt", "$eol$eol").toList()
    .let { it.first().split(eol) to it.last() }
    .let { (map, path) ->
        map.flatMapIndexed { y, it ->
            it.mapIndexedNotNull { x, c ->
                when (c) {
                    '.' -> Tile(x, y)
                    '#' -> Wall(x, y)
                    ' ' -> null
                    else -> throw IllegalArgumentException(c.toString())
                }
            }
        }.toSet() to
            """(\d+|R|L)""".toRegex().findAll(path)
                .flatMap { m ->
                    m.groups.drop(1)
                        .map {
                            when (it?.value) {
                                "R" -> PathTurn(Turn.CLOCKWISE)
                                "L" -> PathTurn(Turn.COUNTERCLOCKWISE)
                                else -> Move22(it?.value?.toInt() ?: throw IllegalArgumentException())
                            }
                        }
                }
    }
