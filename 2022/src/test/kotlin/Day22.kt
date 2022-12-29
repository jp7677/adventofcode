import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private sealed interface Coord22 {
    val x: Int
    val y: Int
    val plane: Int
}

private data class Tile(override val x: Int, override val y: Int, override val plane: Int = 0) : Coord22
private data class Wall(override val x: Int, override val y: Int, override val plane: Int = 0) : Coord22

private enum class Direction { UP, RIGHT, DOWN, LEFT }
private data class Position22(var tile: Tile, var direction: Direction) {
    fun password() = ((tile.y + 1) * 1000) + ((tile.x + 1) * 4) + when (direction) {
        Direction.RIGHT -> 0
        Direction.DOWN -> 1
        Direction.LEFT -> 2
        Direction.UP -> 3
    }
}

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
                is PathTurn -> pos.direction = pos.direction.turn(segment.turn)
                is Move22 -> repeat(segment.steps) {
                    map.peekForMap(pos)?.let {
                        pos.tile = Tile(it.x, it.y)
                    } ?: return@repeat
                }
            }
        }

        pos.password() shouldBe 123046
    }

    "puzzle part 02" {
        val (map, path) = getMapAndPath(includePlanes = true)
        val pos = Position22(Tile(map.filter { it.y == 0 }.minOf { it.x }, 0, 1), Direction.RIGHT)

        path.forEach { segment ->
            when (segment) {
                is PathTurn -> pos.direction = pos.direction.turn(segment.turn)
                is Move22 -> repeat(segment.steps) {
                    map.peekForCube(pos)?.let { (tile, direction) ->
                        pos.tile = tile
                        pos.direction = direction
                    } ?: return@repeat
                }
            }
        }

        pos.password() shouldBe 195032
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

private fun Set<Coord22>.peekForMap(pos: Position22): Coord22? {
    val next = when (pos.direction) {
        Direction.UP -> Tile(pos.tile.x, pos.tile.y.dec())
            .let { if (containsCoord(it)) it else Tile(it.x, this.filter { m -> m.x == it.x }.maxOf { m -> m.y }) }
        Direction.RIGHT -> Tile(pos.tile.x.inc(), pos.tile.y)
            .let { if (containsCoord(it)) it else Tile(this.filter { m -> m.y == it.y }.minOf { m -> m.x }, it.y) }
        Direction.DOWN -> Tile(pos.tile.x, pos.tile.y.inc())
            .let { if (containsCoord(it)) it else Tile(it.x, this.filter { m -> m.x == it.x }.minOf { m -> m.y }) }
        Direction.LEFT -> Tile(pos.tile.x.dec(), pos.tile.y)
            .let { if (containsCoord(it)) it else Tile(this.filter { m -> m.y == it.y }.maxOf { m -> m.x }, it.y) }
    }

    return if (this.contains(next)) next else null
}

private fun Set<Coord22>.containsCoord(tile: Tile) = this.contains(tile) || this.contains(Wall(tile.x, tile.y))

private fun Set<Coord22>.peekForCube(pos: Position22): Position22? {
    val next = when (pos.direction) {
        Direction.UP -> Tile(pos.tile.x, pos.tile.y.dec(), pos.tile.plane)
        Direction.RIGHT -> Tile(pos.tile.x.inc(), pos.tile.y, pos.tile.plane)
        Direction.DOWN -> Tile(pos.tile.x, pos.tile.y.inc(), pos.tile.plane)
        Direction.LEFT -> Tile(pos.tile.x.dec(), pos.tile.y, pos.tile.plane)
    }
        .let { Position22(it, pos.direction) }
        .let { if (isCorrectPlane(it)) it else mapToOtherPlane(it) }

    return if (this.contains(next.tile)) next else null
}

private fun Set<Coord22>.isCorrectPlane(pos: Position22) = any { it.x == pos.tile.x && it.y == pos.tile.y && it.plane == pos.tile.plane }

private fun mapToOtherPlane(pos: Position22): Position22 {
    return when (pos.tile.plane) {
        1 -> when (pos.direction) {
            Direction.UP -> Position22(Tile(0, pos.tile.x + 100, 6), Direction.RIGHT)
            Direction.LEFT -> Position22(Tile(0, (pos.tile.y - 149).absoluteValue, 4), Direction.RIGHT)
            Direction.RIGHT -> pos.copy(tile = pos.tile.copy(plane = 2))
            Direction.DOWN -> pos.copy(tile = pos.tile.copy(plane = 3))
        }
        2 -> when (pos.direction) {
            Direction.UP -> Position22(Tile(pos.tile.x - 100, 199, 6), Direction.UP)
            Direction.RIGHT -> Position22(Tile(99, (pos.tile.y - 149).absoluteValue, 5), Direction.LEFT)
            Direction.DOWN -> Position22(Tile(99, pos.tile.x - 50, 3), Direction.LEFT)
            Direction.LEFT -> pos.copy(tile = pos.tile.copy(plane = 1))
        }
        3 -> when (pos.direction) {
            Direction.LEFT -> Position22(Tile(pos.tile.y - 50, 100, 4), Direction.DOWN)
            Direction.RIGHT -> Position22(Tile(pos.tile.y + 50, 49, 2), Direction.UP)
            Direction.UP -> pos.copy(tile = pos.tile.copy(plane = 1))
            Direction.DOWN -> pos.copy(tile = pos.tile.copy(plane = 5))
        }
        4 -> when (pos.direction) {
            Direction.UP -> Position22(Tile(50, pos.tile.x + 50, 3), Direction.RIGHT)
            Direction.LEFT -> Position22(Tile(50, (pos.tile.y - 149).absoluteValue, 1), Direction.RIGHT)
            Direction.RIGHT -> pos.copy(tile = pos.tile.copy(plane = 5))
            Direction.DOWN -> pos.copy(tile = pos.tile.copy(plane = 6))
        }
        5 -> when (pos.direction) {
            Direction.RIGHT -> Position22(Tile(149, (pos.tile.y - 149).absoluteValue, 2), Direction.LEFT)
            Direction.DOWN -> Position22(Tile(49, pos.tile.x + 100, 6), Direction.LEFT)
            Direction.UP -> pos.copy(tile = pos.tile.copy(plane = 3))
            Direction.LEFT -> pos.copy(tile = pos.tile.copy(plane = 4))
        }
        6 -> when (pos.direction) {
            Direction.RIGHT -> Position22(Tile(pos.tile.y - 100, 149, 5), Direction.UP)
            Direction.DOWN -> Position22(Tile(pos.tile.x + 100, 0, 2), Direction.DOWN)
            Direction.LEFT -> Position22(Tile(pos.tile.y - 100, 0, 1), Direction.DOWN)
            Direction.UP -> pos.copy(tile = pos.tile.copy(plane = 4))
        }
        else -> throw IllegalArgumentException("${pos.tile.plane} - ${pos.direction}")
    }
}

private fun getMapAndPath(includePlanes: Boolean = false) = getPuzzleInput("day22-input.txt", "$eol$eol").toList()
    .let { it.first().split(eol) to it.last() }
    .let { (map, path) ->
        map.flatMapIndexed { y, it ->
            it.mapIndexedNotNull { x, c ->
                val plane = if (x in 50..99 && y in 0..49) 1
                else if (x in 100..149 && y in 0..49) 2
                else if (x in 50..99 && y in 50..99) 3
                else if (x in 0..49 && y in 100..149) 4
                else if (x in 50..99 && y in 100..149) 5
                else if (x in 0..49 && y in 150..199) 6
                else 0

                when (c) {
                    '.' -> Tile(x, y, if (includePlanes) plane else 0)
                    '#' -> Wall(x, y, if (includePlanes) plane else 0)
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
