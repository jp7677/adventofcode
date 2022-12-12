import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Operation(val op: String, val other: String) {
    fun run(level: Long) = when (op) {
        "*" -> level * if (other == "old") level else other.toLong()
        "+" -> level + if (other == "old") level else other.toLong()
        else -> throw IllegalArgumentException()
    }
}

private data class Monkey(
    val items: MutableList<Long>,
    val operation: Operation,
    val test: Int,
    val ifTrue: Int,
    val ifFalse: Int
)

class Day11 : StringSpec({
    "puzzle part 01" { runRound(20, 3) shouldBe 110220 }
    "puzzle part 02" { runRound(10000) shouldBe 19457438264 }
})

private fun runRound(times: Int, worryLevelDivider: Int = 1): Long {
    val monkeys = getMonkeys()
    val inspection = MutableList(monkeys.size) { 0L }
    val primes = monkeys.map { it.test }.reduce { acc, it -> acc * it }

    repeat(times) { _ ->
        monkeys.forEachIndexed { index, m ->
            m.items.forEach { item ->
                inspection[index]++
                val level = (m.operation.run(item) / worryLevelDivider) % primes
                monkeys[if (level % m.test == 0L) m.ifTrue else m.ifFalse].items += level
            }
            m.items.clear()
        }
    }

    return inspection.sorted().takeLast(2).reduce { acc, it -> acc * it }
}

private fun getMonkeys() = getPuzzleInput("day11-input.txt", "$eol$eol")
    .map {
        it.split(eol)
            .drop(1)
            .let { m ->
                Monkey(
                    m[0].drop("  Starting items: ".length)
                        .split(", ").map(String::toLong).toMutableList(),
                    m[1].drop("  Operation: new = old ".length)
                        .split(" ").let { o -> Operation(o.first(), o.last()) },
                    m[2].drop("  Test: divisible by ".length).toInt(),
                    m[3].drop("    If true: throw to monkey ".length).toInt(),
                    m[4].drop("    If false: throw to monkey ".length).toInt()
                )
            }
    }.toList()
