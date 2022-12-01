import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01 : StringSpec({
    val calories = Util.getInputAsListOfString("day01-input.txt")
        .fold(arrayOf(0)) { acc, it ->
            if (it.isNotEmpty())
                acc.apply { this[lastIndex] += it.toInt() }
            else
                acc + arrayOf(0)
        }

    "puzzle part 01" {
        val maxCalories = calories.maxOf { it }

        maxCalories shouldBe 70374
    }

    "puzzle part 02" {
        val max3Calories = calories
            .sortedByDescending { it }
            .take(3)
            .sumOf { it }

        max3Calories shouldBe 204610
    }
})
