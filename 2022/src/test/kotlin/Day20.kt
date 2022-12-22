import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day20 : StringSpec({
    "puzzle part 01".config(enabled = false) {
        val numbers = getPuzzleInput("day20-input.txt").withIndex()
            .map { "${it.index}#${it.value}" }
            .toList()

        val file = numbers.zipWithNext().toMap()
            .plus(numbers.last() to numbers.first())
            .toMutableMap()

        numbers.indices.forEach {
            val number = numbers[it]
            if (number.toValue() % numbers.size != 0)
                file.move(number)
        }

        val zero = file.keys.single { it.endsWith("#0") }
        val grove1 = file.keyAfter(zero, 1000)
        val grove2 = file.keyAfter(zero, 2000)
        val grove3 = file.keyAfter(zero, 3000)

        grove1.toValue() + grove2.toValue() + grove3.toValue() shouldBe 13148 // 7716 too low // 13148 not good??
    }
})

private fun MutableMap<String, String>.move(number: String) {
    val value = number.toValue() % size

    val gap1 = keyBefore(number, 1) // alternative: keyAfter(number, size - 1)
    val gap2 = this[number]!!
    val between1 = if (value > 0) keyAfter(number, value) else keyAfter(number, size + value - 1)
    val between2 = this[between1]!!

    this[gap1] = gap2
    this[between1] = number
    this[number] = between2
}

private fun Map<String, String>.keyAfter(start: String, moves: Int): String {
    var current = start
    repeat(moves) {
        current = this[current]!!
    }
    return current
}

private fun Map<String, String>.keyBefore(start: String, moves: Int): String {
    var current = start
    repeat(moves) {
        current = entries.single { it.value == current }.key
    }
    return current
}

private fun String.toValue() = split('#')[1].toInt()

private fun MutableMap<String, String>.debug() {
    val start = entries.last().key
    (0 until size).forEach {
        print("${this[keyAfter(start, it)]!!.toValue()}, ")
    }
    println()
}
