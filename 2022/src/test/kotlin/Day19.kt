import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Stash(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geode: Int = 0
) {
    fun any() = ore > 0 || clay > 0 || obsidian > 0 || geode > 0
}

private data class CollectState(
    val materials: Stash = Stash(),
    val robots: Stash = Stash(ore = 1),
    val factory: Stash = Stash()
)

private data class Blueprint(
    val id: Int,
    val oreRobotOreCosts: Int,
    val clayRobotOreCosts: Int,
    val obsidianRobotOreCosts: Int,
    val obsidianRobotClayCosts: Int,
    val geodeRobotOreCosts: Int,
    val geodeRobotObsidianCosts: Int
)

class Day19 : StringSpec({
    "puzzle part 01".config(enabled = false) {
        val blueprints = getBlueprints()

        val geodes = blueprints.map {
            val materials = collectMaterials(it, 24)
            it.id to materials.geode
        }

        geodes shouldBe idToMaxGeode
        geodes.sumOf { it.first * it.second } shouldBe 2301
        blueprints.count() shouldBe 30
    }
})

private fun collectMaterials(blueprint: Blueprint, minutes: Int): Stash {
    val states = mutableSetOf(CollectState())
    repeat(minutes) {
        // needed for id 23, but yielded wrong results for others
//        val invalidStates = mutableListOf<CollectState>()
//        if (24 - minute < blueprint.geodeRobotObsidianCosts) {
//            invalidStates.addAll(states.filter { it.robots.obsidian == 0 })
//        }
//        states.removeAll(invalidStates.toSet())

        states.forEach { state ->
            if (state.materials.obsidian >= blueprint.geodeRobotObsidianCosts && state.materials.ore >= blueprint.geodeRobotOreCosts) {
                state.factory.geode += 1
                state.materials.obsidian -= blueprint.geodeRobotObsidianCosts
                state.materials.ore -= blueprint.geodeRobotOreCosts
            }
        }

        val newStates = mutableListOf<CollectState>()
        states.forEach { state ->
            if (!state.factory.any()) {
                if (state.materials.clay >= blueprint.obsidianRobotClayCosts && state.materials.ore >= blueprint.obsidianRobotOreCosts) {
                    val state1 = CollectState(state.materials.copy(), state.robots.copy(), state.factory.copy())
                    state1.factory.obsidian += 1
                    state1.materials.clay -= blueprint.obsidianRobotClayCosts
                    state1.materials.ore -= blueprint.obsidianRobotOreCosts
                    newStates.add(state1)
                }
                if (state.materials.ore >= blueprint.clayRobotOreCosts) {
                    val state2 = CollectState(state.materials.copy(), state.robots.copy(), state.factory.copy())
                    state2.factory.clay += 1
                    state2.materials.ore -= blueprint.clayRobotOreCosts
                    newStates.add(state2)
                }
                if (state.materials.ore >= blueprint.oreRobotOreCosts) {
                    val state3 = CollectState(state.materials.copy(), state.robots.copy(), state.factory.copy())
                    state3.factory.ore += 1
                    state3.materials.ore -= blueprint.oreRobotOreCosts
                    newStates.add(state3)
                }
            }
        }

        states.addAll(newStates)

        states.forEach { state ->
            state.materials.ore += state.robots.ore
            state.materials.clay += state.robots.clay
            state.materials.obsidian += state.robots.obsidian
            state.materials.geode += state.robots.geode

            state.robots.ore += state.factory.ore.also { state.factory.ore = 0 }
            state.robots.clay += state.factory.clay.also { state.factory.clay = 0 }
            state.robots.obsidian += state.factory.obsidian.also { state.factory.obsidian = 0 }
            state.robots.geode += state.factory.geode.also { state.factory.geode = 0 }
        }
    }

    return if (states.any()) states.maxByOrNull { it.materials.geode }!!.materials else Stash()
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
