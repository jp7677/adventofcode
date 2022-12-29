import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Coord23(val x: Int, val y: Int) {
    operator fun plus(other: Coord23) = Coord23(this.x + other.x, this.y + other.y)
}

private enum class Turn23(val value: Int) {
    NORTH(0), SOUTH(1), WEST(2), EAST(3);
    companion object {
        fun from(value: Int) = values().single { it.value == value }
    }
}

private val northWest = Coord23(-1, -1)
private val north = Coord23(0, -1)
private val northEast = Coord23(1, -1)
private val east = Coord23(1, 0)
private val southEast = Coord23(1, 1)
private val south = Coord23(0, 1)
private val southWest = Coord23(-1, 1)
private val west = Coord23(-1, 0)

private val adjacent = listOf(northWest, north, northEast, east, southEast, south, southWest, west)

class Day23 : StringSpec({
    "puzzle part 01" {
        var map = getMap()

        repeat(10) { round ->
            val moves = getMoves(Turn23.from(round % 4), map)
            map = map.map { moves[it] ?: it }.toSet()
        }

        map.countElfs() shouldBe 3990
    }

    "puzzle part 02" {
        val map = getMap()

        val rounds = generateSequence(map to 0) { (map, index) ->
            val moves = getMoves(Turn23.from(index % 4), map)
            if (moves.any()) map.map { moves[it] ?: it }.toSet() to index.inc()
            else null
        }.last().second.inc()

        rounds shouldBe 1057
    }
})

private fun getMoves(turn: Turn23, map: Set<Coord23>): Map<Coord23, Coord23?> {
    val proposals = when (turn) {
        Turn23.NORTH -> map.associateWith { elf ->
            if (map.noneAdjacent(elf)) null
            else if (map.noneNorth(elf)) elf + north
            else if (map.noneSouth(elf)) elf + south
            else if (map.noneWest(elf)) elf + west
            else if (map.noneEast(elf)) elf + east
            else null
        }
        Turn23.SOUTH -> map.associateWith { elf ->
            if (map.noneAdjacent(elf)) null
            else if (map.noneSouth(elf)) elf + south
            else if (map.noneWest(elf)) elf + west
            else if (map.noneEast(elf)) elf + east
            else if (map.noneNorth(elf)) elf + north
            else null
        }
        Turn23.WEST -> map.associateWith { elf ->
            if (map.noneAdjacent(elf)) null
            else if (map.noneWest(elf)) elf + west
            else if (map.noneEast(elf)) elf + east
            else if (map.noneNorth(elf)) elf + north
            else if (map.noneSouth(elf)) elf + south
            else null
        }
        Turn23.EAST -> map.associateWith { elf ->
            if (map.noneAdjacent(elf)) null
            else if (map.noneEast(elf)) elf + east
            else if (map.noneNorth(elf)) elf + north
            else if (map.noneSouth(elf)) elf + south
            else if (map.noneWest(elf)) elf + west
            else null
        }
    }

    return proposals.values
        .groupingBy { it }.eachCount()
        .filter { it.value > 1 }.keys
        .let { overlapping ->
            proposals
                .filterNot { it.value in overlapping }
                .filterValues { it != null }
        }
}

private fun Set<Coord23>.noneAdjacent(coord23: Coord23) = adjacent.none { d -> contains(coord23 + d) }

private fun Set<Coord23>.noneNorth(coord23: Coord23) =
    !contains(coord23 + north) && !contains(coord23 + northEast) && !contains(coord23 + northWest)

private fun Set<Coord23>.noneSouth(coord23: Coord23) =
    !contains(coord23 + south) && !contains(coord23 + southEast) && !contains(coord23 + southWest)

private fun Set<Coord23>.noneWest(coord23: Coord23) =
    !contains(coord23 + west) && !contains(coord23 + northWest) && !contains(coord23 + southWest)

private fun Set<Coord23>.noneEast(coord23: Coord23) =
    !contains(coord23 + east) && !contains(coord23 + northEast) && !contains(coord23 + southEast)

private fun Set<Coord23>.countElfs(): Int {
    val minX = minOf { it.x }
    val maxX = maxOf { it.x }
    val minY = minOf { it.y }
    val maxY = maxOf { it.y }

    return ((maxX - minX + (if (minX < 0) 1 else 0)) * (maxY - minY + (if (minY < 0) 1 else 0))) - count()
}

private fun getMap() = getPuzzleInput("day23-input.txt")
    .flatMapIndexed { y, it -> it.mapIndexedNotNull { x, c -> if (c == '#') Coord23(x, y) else null } }
    .toSet()
