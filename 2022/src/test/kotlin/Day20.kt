import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day20 : StringSpec({
    "puzzle part 01" {
        val (file, mix) = getFile()

        file.indices.forEach { mix.move(file[it]) }

        mix.sumOfGroveCoordinates() shouldBe 9945
    }

    "puzzle part 02" {
        val (file, mix) = getFile(811589153)

        repeat(10) {
            file.indices.forEach { mix.move(file[it]) }
        }

        mix.sumOfGroveCoordinates() shouldBe 3338877775442
    }
})

private fun MutableMap<String, String>.move(number: String) {
    val value = number.toValue() % (size - 1)
    if (value % size == 0L) return

    val steps = if (value > 0) value.toInt() else size + value.toInt() - 1
    val dest = keyAfter(number, steps)

    this[keyBefore(number)] = this[number]!!
    this[number] = this[dest]!!
    this[dest] = number
}

private fun Map<String, String>.keyAfter(number: String, moves: Int): String {
    var current = number
    repeat(moves) { current = this[current]!! }
    return current
}

private fun Map<String, String>.keyBefore(number: String) = entries.single { it.value == number }.key

private fun String.toValue() = split('#')[1].toLong()

private fun MutableMap<String, String>.sumOfGroveCoordinates(): Long {
    val zero = keys.single { it.endsWith("#0") }
    return listOf(1000, 2000, 3000).sumOf { keyAfter(zero, it).toValue() }
}

private fun getFile(key: Int = 1): Pair<List<String>, MutableMap<String, String>> {
    val file = getPuzzleInput("day20-input.txt")
        .map { it.toLong() * key }.withIndex()
        .map { "${it.index}#${it.value}" }
        .toList()

    val mix = file.zipWithNext().toMap()
        .plus(file.last() to file.first())
        .toMutableMap()

    return file to mix
}
