import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private interface Monkey21 {
    fun solve(monkeys: Map<String, Monkey21>): Long
}

private data class NumMonkey21(val num: Long) : Monkey21 {
    override fun solve(monkeys: Map<String, Monkey21>) = num
}

private data class OpMonkey21(val name1: String, val name2: String, val op: (Long, Long) -> Long) : Monkey21 {
    override fun solve(monkeys: Map<String, Monkey21>) =
        op(monkeys[name1]!!.solve(monkeys), monkeys[name2]!!.solve(monkeys))
}

class Day21 : StringSpec({
    "puzzle part 01" {
        val monkeys = getMonkeys()

        val number = monkeys["root"]!!.solve(monkeys)

        number shouldBe 145167969204648
    }
})

private fun getMonkeys() = getPuzzleInput("day21-input.txt")
    .map {
        val s = it.split(": ")
        val name = s.first()
        val num = s.last().toLongOrNull()

        if (num != null)
            name to NumMonkey21(num)
        else {
            val op = it[11]
            val s1 = s.last().split(' ')
            when (op) {
                '+' -> name to OpMonkey21(s1.first(), s1.last(), Long::plus)
                '-' -> name to OpMonkey21(s1.first(), s1.last(), Long::minus)
                '*' -> name to OpMonkey21(s1.first(), s1.last(), Long::times)
                '/' -> name to OpMonkey21(s1.first(), s1.last(), Long::div)
                else -> throw IllegalArgumentException(op.toString())
            }
        }
    }
    .toMap()
