import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue

private data class Tree(val x: Int, val y: Int, val height: Int)

class Day08 : StringSpec({
    "puzzle part 01" {
        val trees = getTrees()

        val countOfVisibleTrees = trees.count { tree ->
            val hidden = trees.any { it.y == tree.y && it.x < tree.x && it.height >= tree.height } &&
                trees.any { it.y == tree.y && it.x > tree.x && it.height >= tree.height } &&
                trees.any { it.y < tree.y && it.x == tree.x && it.height >= tree.height } &&
                trees.any { it.y > tree.y && it.x == tree.x && it.height >= tree.height }
            !hidden
        }

        countOfVisibleTrees shouldBe 1798
    }

    "puzzle part 02" {
        val trees = getTrees()
        val maxX = trees.maxOf { it.x }
        val maxY = trees.maxOf { it.y }

        val score = trees
            .filterNot { it.x == 0 || it.x == maxX || it.y == 0 || it.y == maxY }
            .maxOf { tree ->
                val left = trees.filter { it.y == tree.y && it.x < tree.x }.sortedByDescending { it.x }
                val right = trees.filter { it.y == tree.y && it.x > tree.x }.sortedBy { it.x }
                val up = trees.filter { it.y < tree.y && it.x == tree.x }.sortedByDescending { it.y }
                val down = trees.filter { it.y > tree.y && it.x == tree.x }.sortedBy { it.y }

                val leftScore = left
                    .firstOrNull { it.height >= tree.height }?.let { (tree.x - it.x).absoluteValue } ?: tree.x
                val rightScore = right
                    .firstOrNull { it.height >= tree.height }?.let { (tree.x - it.x).absoluteValue } ?: (maxX - tree.x)
                val upScore = up
                    .firstOrNull { it.height >= tree.height }?.let { (tree.y - it.y).absoluteValue } ?: tree.y
                val downScore = down
                    .firstOrNull { it.height >= tree.height }?.let { (tree.y - it.y).absoluteValue } ?: (maxY - tree.y)

                leftScore * rightScore * upScore * downScore
            }

        score shouldBe 259308
    }
})

private fun getTrees() = getPuzzleInput("day08-input.txt")
    .flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Tree(x, y, c.digitToInt()) }
    }
    .toList()
