import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KFunction

private sealed interface Monkey21 {
    var monkeys: Map<String, Monkey21>
    fun yell(): Long
}

private data class NumMonkey21(val num: Long) : Monkey21 {
    override lateinit var monkeys: Map<String, Monkey21>
    override fun yell() = num
}

private data class OpMonkey21(val name1: String, val name2: String, val op: (Long, Long) -> Long) : Monkey21 {
    override lateinit var monkeys: Map<String, Monkey21>
    override fun yell() = op(monkeys[name1]!!.yell(), monkeys[name2]!!.yell())

    fun revertYell(yell: Long, num: Long, r: Boolean = false) = when ((op as KFunction<*>).name) {
        "plus" -> yell - num
        "minus" -> if (r) yell + num else num - yell
        "times" -> yell / num
        "div" -> if (r) yell * num else num / yell
        else -> throw IllegalStateException()
    }
}

class Day21 : StringSpec({
    "puzzle part 01" {
        val monkeys = getMonkeys()
        monkeys.onEach { it.value.monkeys = monkeys }

        val number = monkeys["root"]!!.yell()

        number shouldBe 145167969204648
    }

    "puzzle part 02" {
        val monkeys = getMonkeys()
        monkeys.onEach { it.value.monkeys = monkeys }

        val root = monkeys["root"]!! as OpMonkey21
        val name = if (monkeys.hasHuman(root.name1)) root.name1 else root.name2
        val number = root.revertYell(root.yell(), monkeys[name]!!.yell())

        val humanYell = monkeys.findHuman(monkeys[name]!!, number)

        humanYell shouldBe 3330805295850
    }
})

private fun Map<String, Monkey21>.findHuman(monkey: Monkey21, yell: Long): Long {
    if (monkey !is OpMonkey21) throw IllegalStateException()

    val name1HasHuman = this.hasHuman(monkey.name1)
    val name = if (name1HasHuman) monkey.name1 else monkey.name2
    val otherYell = this[if (name1HasHuman) monkey.name2 else monkey.name1]!!.yell()
    val number = monkey.revertYell(yell, otherYell, name1HasHuman)

    if (name == "humn") return number
    return findHuman(this[name]!!, number)
}

private fun Map<String, Monkey21>.hasHuman(name: String): Boolean {
    if (name == "humn") return true

    return when (val monkey = this[name]!!) {
        is OpMonkey21 -> hasHuman(monkey.name1) || hasHuman(monkey.name2)
        is NumMonkey21 -> false
    }
}

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
