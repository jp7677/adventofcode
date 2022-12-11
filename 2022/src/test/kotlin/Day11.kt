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
    "puzzle part 01" {
        val monkeys = getMonkeys()
        val inspection = MutableList(monkeys.size) { 0L }

        repeat(20) { _ ->
            monkeys.forEachIndexed { index, m ->
                m.items.forEach { i ->
                    inspection[index]++
                    val level = m.operation.run(i) / 3
                    monkeys[if (level % m.test == 0L) m.ifTrue else m.ifFalse].items += level
                }
                m.items.clear()
            }
        }

        val monkeyBusinessLevel = inspection.sorted().takeLast(2).reduce { acc, it -> acc * it }

        monkeyBusinessLevel shouldBe 110220
    }

    "puzzle part 02" {
        val monkeys = getMonkeys()
        val inspection = MutableList(monkeys.size) { 0L }
        val primes = monkeys.map { it.test }.reduce { acc, it -> acc * it }

        repeat(10000) { _ ->
            monkeys.forEachIndexed { index, m ->
                m.items.forEach { i ->
                    inspection[index]++
                    val level = m.operation.run(i) % primes
                    monkeys[if (level % m.test == 0L) m.ifTrue else m.ifFalse].items += level
                }
                m.items.clear()
            }
        }

        val monkeyBusinessLevel = inspection.sorted().takeLast(2).reduce { acc, it -> acc * it }

        monkeyBusinessLevel shouldBe 19457438264
    }
})

private fun getMonkeys() = getPuzzleInput("day11-input.txt", "$eol$eol")
    .toList()
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
    }
