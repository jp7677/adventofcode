import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01 : StringSpec({
    "puzzle part 01" {
        val maxCalories = getCalories().maxOf { it }

        maxCalories shouldBe 70374
    }

    "puzzle part 02" {
        val max3Calories = getCalories()
            .sortedByDescending { it }
            .take(3)
            .sumOf { it }

        max3Calories shouldBe 204610
    }
})

private fun getCalories() = Util.getInputAsListOfString("day01-input.txt")
    .fold(arrayOf(0)) { acc, it ->
        if (it.isNotEmpty())
            acc.apply { this[lastIndex] += it.toInt() }
        else
            acc + arrayOf(0)
    }
