import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day08 : StringSpec({
    "puzzle part 01" {
        val (trees, maxX, maxY) = getTreesWithSizing()

        val countOfVisibleTrees = (0..maxX).sumOf { x ->
            (0..maxY).count { y ->
                val height = trees[x][y]
                (x - 1 downTo 0).none { trees[it][y] >= height } ||
                    (x + 1..maxX).none { trees[it][y] >= height } ||
                    (y - 1 downTo 0).none { trees[x][it] >= height } ||
                    (y + 1..maxY).none { trees[x][it] >= height }
            }
        }

        countOfVisibleTrees shouldBe 1798
    }

    "puzzle part 02" {
        val (trees, maxX, maxY) = getTreesWithSizing()

        val maxScore = (1 until maxX).flatMap { x ->
            (1 until maxY).map { y ->
                val height = trees[x][y]
                val left = (x - 1 downTo 0).firstOrNull { trees[it][y] >= height }?.let { x - it } ?: x
                val right = (x + 1..maxX).firstOrNull { trees[it][y] >= height }?.let { it - x } ?: (maxX - x)
                val up = (y - 1 downTo 0).firstOrNull { trees[x][it] >= height }?.let { y - it } ?: y
                val down = (y + 1..maxY).firstOrNull { trees[x][it] >= height }?.let { it - y } ?: (maxY - y)
                left * right * up * down
            }
        }.max()

        maxScore shouldBe 259308
    }
})

private fun getTreesWithSizing() = getPuzzleInput("day08-input.txt")
    .map { it.map(Char::digitToInt).toIntArray() }
    .toList().toTypedArray()
    .let { Triple(it, it[0].size - 1, it.size - 1) }
