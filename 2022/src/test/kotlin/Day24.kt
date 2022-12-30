import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private enum class Direction24 { NORTH, SOUTH, WEST, EAST }
private sealed interface Coord24 {
    val x: Int
    val y: Int
}

private data class Blizzard(override val x: Int, override val y: Int, val direction: Direction24) : Coord24
private data class Expedition(override val x: Int, override val y: Int) : Coord24

class Day24 : StringSpec({
    "puzzle part 01" {
        val (map, maxX, maxY) = getMap()

        val minutes = reachGoal(map, maxX, maxY, Expedition(1, 1), Expedition(maxX - 1, maxY - 1))

        minutes shouldBe 288
    }

    "puzzle part 02" {
        val (map, maxX, maxY) = getMap()

        val minutes1 = reachGoal(map, maxX, maxY, Expedition(1, 1), Expedition(maxX - 1, maxY - 1))
        val minutes2 = reachGoal(map, maxX, maxY, Expedition(maxX - 1, maxY - 1), Expedition(1, 1))
        val minutes3 = reachGoal(map, maxX, maxY, Expedition(1, 1), Expedition(maxX - 1, maxY - 1))

        minutes1 + minutes2 + minutes3 shouldBe 861
    }
})

private fun reachGoal(map: MutableList<Blizzard>, maxX: Int, maxY: Int, start: Expedition, end: Expedition): Int {
    val ex = mutableSetOf(start)

    var minutes = 0
    minutes += map.moveBlizzards(maxX, maxY)
    while (!ex.contains(end)) {
        minutes += map.moveBlizzards(maxX, maxY)
        ex.toSet().forEach {
            if (map.any { b -> b.x == it.x && b.y == it.y }) ex.remove(it)
            if (map.canMoveEast(it, maxX)) ex.add(Expedition(it.x.inc(), it.y))
            if (map.canMoveSouth(it, maxY)) ex.add(Expedition(it.x, it.y.inc()))
            if (map.canMoveNorth(it)) ex.add(Expedition(it.x, it.y.dec()))
            if (map.canMoveWest(it)) ex.add(Expedition(it.x.dec(), it.y))
        }
        if (map.none { it.x == start.x && it.y == start.y }) ex.add(start)
    }
    minutes += map.moveBlizzards(maxX, maxY)
    return minutes
}

private fun List<Blizzard>.canMoveWest(ex: Coord24) = ex.x > 1 &&
    none { it.x == ex.x.dec() && it.y == ex.y }

private fun List<Blizzard>.canMoveNorth(ex: Coord24) = ex.y > 1 &&
    none { it.x == ex.x && it.y == ex.y.dec() }

private fun List<Blizzard>.canMoveSouth(ex: Coord24, maxY: Int) = ex.y < maxY - 1 &&
    none { it.x == ex.x && it.y == ex.y.inc() }

private fun List<Blizzard>.canMoveEast(ex: Coord24, maxX: Int) = ex.x < maxX - 1 &&
    none { it.x == ex.x.inc() && it.y == ex.y }

private fun MutableList<Blizzard>.moveBlizzards(maxX: Int, maxY: Int): Int {
    val new = this.map {
        val next = when (it.direction) {
            Direction24.NORTH -> it.copy(y = it.y.dec())
            Direction24.SOUTH -> it.copy(y = it.y.inc())
            Direction24.WEST -> it.copy(x = it.x.dec())
            Direction24.EAST -> it.copy(x = it.x.inc())
        }

        if (next.x == 0) next.copy(x = maxX - 1)
        else if (next.x == maxX) next.copy(x = 1)
        else if (next.y == 0) next.copy(y = maxY - 1)
        else if (next.y == maxY) next.copy(y = 1)
        else next
    }
    this.clear()
    this.addAll(new)
    return 1
}

private fun getMap() = getPuzzleInput("day24-input.txt").toList()
    .let { s ->
        Triple(
            s.flatMapIndexed { y, it ->
                it.mapIndexedNotNull { x, c ->
                    when (c) {
                        '>' -> Blizzard(x, y, Direction24.EAST)
                        'v' -> Blizzard(x, y, Direction24.SOUTH)
                        '<' -> Blizzard(x, y, Direction24.WEST)
                        '^' -> Blizzard(x, y, Direction24.NORTH)
                        else -> null
                    }
                }
            }.toMutableList(),
            s.first().length - 1,
            s.count() - 1
        )
    }
