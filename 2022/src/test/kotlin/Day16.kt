import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Valve(val name: String, val flowRate: Int, val leadsTo: List<String>, var open: Boolean = false)
private val re = "rate=(\\d+);".toRegex()

class Day16 : StringSpec({
    "puzzle part 01" {
        val valves = getValves()

        val start = valves.single { it.name == "AA" }
        val relevantValves = valves.filterNot { it.flowRate == 0 }.sortedByDescending { it.flowRate }.toSet()
        val distances = (relevantValves + start).flatMap { v1 ->
            (relevantValves + start).map { v2 -> Pair(v1.name, v2.name) to valves.distance(v1, v2) }
        }.toMap()

        val time = 30
        val maxPressure = relevantValves.allRoutes(start, listOf(), time, distances)
            .maxOf { it.pressure(time) }

        maxPressure shouldBe 1728
    }

    "puzzle part 02" {
        val valves = getValves()

        val start = valves.single { it.name == "AA" }
        val relevantValves = valves.filterNot { it.flowRate == 0 }.sortedByDescending { it.flowRate }.toSet()
        val distances = (relevantValves + start).flatMap { v1 ->
            (relevantValves + start).map { v2 -> Pair(v1.name, v2.name) to valves.distance(v1, v2) }
        }.toMap()

        val time = 26
        val pressureForOne = relevantValves
            .allRoutes(start, listOf(), time, distances)
            .sortedByDescending { it.pressure(time) }
            .take(1000) // Just a gamble but seems to work

        val combinedPressure = pressureForOne
            .flatMap { elf ->
                pressureForOne
                    .filter { elephant -> elf.none { elephant.any { e -> e.first.name == it.first.name } } }
                    .map { elephant -> elf to elephant }
            }
            .maxOf {
                it.first.pressure(time) + it.second.pressure(time)
            }

        combinedPressure shouldBe 2304
    }
})

private fun Set<Valve>.distance(start: Valve, end: Valve) = routesWithoutLoops(start, end)
    .filter { v -> v.any { it.name == end.name } }
    .minOf { v -> v.indexOfFirst { it.name == end.name } }

private fun Set<Valve>.routesWithoutLoops(
    current: Valve,
    end: Valve,
    path: List<Valve> = listOf(),
    knownPaths: List<List<Valve>> = listOf()
): List<List<Valve>> {
    if (current == end) return listOf(path + current)

    val next = current.leadsTo.filterNot { it in path.map { n -> n.name } }
    if (next.none()) return listOf(path + current)

    return knownPaths + next.flatMap {
        routesWithoutLoops(this.single { n -> n.name == it }, end, path + current, knownPaths)
    }
}

private fun Set<Valve>.allRoutes(
    current: Valve,
    path: List<Pair<Valve, Int>>,
    max: Int,
    distances: Map<Pair<String, String>, Int>,
    knownPaths: Set<List<Pair<Valve, Int>>> = setOf()
): Set<List<Pair<Valve, Int>>> {
    val next = this
        .filterNot { it.name == current.name || it.name in path.map { n -> n.first.name } }
        .map {
            val distance = if (path.isEmpty())
                distances["AA" to it.name]!!
            else
                path.last().second + distances[path.last().first.name to it.name]!!

            it to distance
        }
        .filter { it.second < max }

    if (next.none())
        return setOf(path)

    return knownPaths + next.flatMap {
        allRoutes(
            this.single { n -> n.name == it.first.name },
            path + (it.first to it.second.inc()),
            max,
            distances,
            knownPaths
        )
    }
}

private fun List<Pair<Valve, Int>>.pressure(minutes: Int) = this.fold(0L) { acc, it ->
    acc + ((minutes - it.second) * it.first.flowRate)
}

private fun getValves() = getPuzzleInput("day16-input.txt")
    .map {
        val n = it.substring(6..7)
        val r = re.find(it)?.groups?.get(1)?.value?.toInt() ?: throw IllegalArgumentException(it)
        val l = it.drop(it.indexOf(";") + 24).split(", ").map { s -> s.trim() }
        Valve(n, r, l)
    }.toSet()
