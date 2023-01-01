import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Blueprint(
    val id: Int,
    val oreRobotOreCosts: Int,
    val clayRobotOreCosts: Int,
    val obsidianRobotOreCosts: Int,
    val obsidianRobotClayCosts: Int,
    val geodeRobotOreCosts: Int,
    val geodeRobotObsidianCosts: Int
)

private data class Stock(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geode: Int = 0
)

private enum class Producing { NONE, ORE_ROBOT, CLAY_ROBOT, OBSIDIAN_ROBOT, GEODE_ROBOT }

private data class CollectState(
    val blueprint: Blueprint,
    val robots: Stock = Stock(ore = 1),
    val materials: Stock = Stock(),
    var factory: Producing = Producing.NONE
) {
    fun copy() = CollectState(blueprint, robots.copy(), materials.copy(), factory)
}

class Day19 : StringSpec({
    "puzzle part 01" {
        val blueprints = getBlueprints().asSequence()

        val geodes = blueprints.map { it.id to collectMaterials(it, 24).geode }.toList()

        geodes shouldBe idToMaxGeode
        geodes.sumOf { it.first * it.second } shouldBe 2301
    }

    "puzzle part 02" {
        val blueprints = getBlueprints().asSequence().take(3)

        val geodes = blueprints
            .map { collectMaterials(it, 32).geode }
            .reduce { acc, it -> acc * it }

        geodes shouldBe 10336
    }
})

private fun collectMaterials(blueprint: Blueprint, minutes: Int): Stock {
    val states = mutableSetOf(CollectState(blueprint))
    repeat(minutes) { _ ->
        states.removeNonPromising()

        states.forEach {
            if (it.canProduceGeodeRobot()) it.produceGeodeRobot()
        }

        states.addAll(
            buildList {
                states.forEach {
                    if (it.canProduceObsidianRobot()) add(it.copy().apply { produceObsidianRobot() })
                    if (it.canProduceClayRobot()) add(it.copy().apply { produceClayRobot() })
                    if (it.canProduceOreRobot()) add(it.copy().apply { produceOreRobot() })
                }
            }
        )

        states.forEach {
            it.collectMaterials()
            it.deliverRobot()
        }
    }

    return states.maxBy { it.materials.geode }.materials
}

private fun MutableSet<CollectState>.removeNonPromising() {
    val maxPromisingStates = 1500 // Arbitrary number based on playing with the results
    if (count() <= maxPromisingStates) return

    val promising = sortedWith(
        compareByDescending<CollectState> { it.materials.geode }
            .thenByDescending { it.robots.geode }
            .thenByDescending { it.robots.obsidian }
            .thenByDescending { it.robots.clay }
            .thenByDescending { it.robots.ore }
    ).take(maxPromisingStates)

    removeIf { it !in promising }
}

private fun CollectState.canProduceOreRobot() = factory == Producing.NONE &&
    materials.ore >= blueprint.oreRobotOreCosts

private fun CollectState.canProduceClayRobot() = factory == Producing.NONE &&
    materials.ore >= blueprint.clayRobotOreCosts

private fun CollectState.canProduceObsidianRobot() = factory == Producing.NONE &&
    materials.clay >= blueprint.obsidianRobotClayCosts && materials.ore >= blueprint.obsidianRobotOreCosts

private fun CollectState.canProduceGeodeRobot() = factory == Producing.NONE &&
    materials.obsidian >= blueprint.geodeRobotObsidianCosts && materials.ore >= blueprint.geodeRobotOreCosts

private fun CollectState.produceOreRobot() {
    factory = Producing.ORE_ROBOT
    materials.ore -= blueprint.oreRobotOreCosts
}

private fun CollectState.produceClayRobot() {
    factory = Producing.CLAY_ROBOT
    materials.ore -= blueprint.clayRobotOreCosts
}

private fun CollectState.produceObsidianRobot() {
    factory = Producing.OBSIDIAN_ROBOT
    materials.clay -= blueprint.obsidianRobotClayCosts
    materials.ore -= blueprint.obsidianRobotOreCosts
}

private fun CollectState.produceGeodeRobot() {
    factory = Producing.GEODE_ROBOT
    materials.obsidian -= blueprint.geodeRobotObsidianCosts
    materials.ore -= blueprint.geodeRobotOreCosts
}

private fun CollectState.collectMaterials() {
    materials.ore += robots.ore
    materials.clay += robots.clay
    materials.obsidian += robots.obsidian
    materials.geode += robots.geode
}

private fun CollectState.deliverRobot() {
    when (factory) {
        Producing.NONE -> return
        Producing.ORE_ROBOT -> robots.ore++
        Producing.CLAY_ROBOT -> robots.clay++
        Producing.OBSIDIAN_ROBOT -> robots.obsidian++
        Producing.GEODE_ROBOT -> robots.geode++
    }
    factory = Producing.NONE
}

private fun getBlueprints(): List<Blueprint> {
    val blueprints = getPuzzleInput("day19-input.txt").toList()
        .map { s ->
            """(\d+)""".toRegex().findAll(s)
                .map {
                    it.groupValues.first().toInt()
                }
                .toList()
                .let {
                    Blueprint(it[0], it[1], it[2], it[3], it[4], it[5], it[6])
                }
        }
    return blueprints
}

private val idToMaxGeode = listOf(
    1 to 3,
    2 to 0,
    3 to 0,
    4 to 2,
    5 to 9,
    6 to 2,
    7 to 1,
    8 to 0,
    9 to 6,
    10 to 1,
    11 to 15,
    12 to 0,
    13 to 9,
    14 to 11,
    15 to 0,
    16 to 0,
    17 to 12,
    18 to 0,
    19 to 0,
    20 to 3,
    21 to 3,
    22 to 1,
    23 to 16,
    24 to 0,
    25 to 7,
    26 to 9,
    27 to 5,
    28 to 0,
    29 to 15,
    30 to 1
)
