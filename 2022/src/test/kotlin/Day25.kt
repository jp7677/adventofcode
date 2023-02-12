import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.pow

class Day25 : StringSpec({
    "puzzle part 01" {
        val snafuNumber = getPuzzleInput("day25-input.txt")
            .map { it.fromSnafuNumber() }
            .sum().toSnafuNumber()

        snafuNumber shouldBe "20=212=1-12=200=00-1"
    }
})

private fun String.fromSnafuNumber() = this.reversed()
    .mapIndexed { index, c ->
        when (c) {
            '-' -> -1
            '=' -> -2
            else -> c.digitToInt()
        } * (5.0.pow(index)).toLong()
    }
    .sum()

private fun Long.toSnafuNumber() = generateSequence(quotientToRemainder()) { (q, _) ->
    (q / 5).let {
        if (it != 0L) it.quotientToRemainder()
        else null
    }
}
    .map { it.second }
    .toList().reversed()
    .joinToString("")
    .trimStart('0')

private fun Long.quotientToRemainder() = when (val r = this % 5) {
    4L -> this + 1 to "-"
    3L -> this + 2 to "="
    else -> this to r.toString()
}
