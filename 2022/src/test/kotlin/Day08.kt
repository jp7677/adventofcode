import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private data class Tree(val x: Int, val y: Int, val height: Int)

class Day08 : StringSpec({
    "puzzle part 01" {
        val trees = getTrees()

        val countOfVisibleTrees = trees.count { tree ->
            trees.none { it.y == tree.y && it.x < tree.x && it.height >= tree.height } ||
                trees.none { it.y == tree.y && it.x > tree.x && it.height >= tree.height } ||
                trees.none { it.y < tree.y && it.x == tree.x && it.height >= tree.height } ||
                trees.none { it.y > tree.y && it.x == tree.x && it.height >= tree.height }
        }

        countOfVisibleTrees shouldBe 1798
    }

    "puzzle part 02" {
        val trees = getTrees()
        val treesReversed = getTrees().reversed()
        val maxX = trees.maxOf { it.x }
        val maxY = trees.maxOf { it.y }

        val score = trees
            .filterNot { it.x == 0 || it.x == maxX || it.y == 0 || it.y == maxY }
            .maxOf { tree ->
                val left = treesReversed.filter { it.y == tree.y && it.x < tree.x }
                    .firstOrNull { it.height >= tree.height }
                    ?.let { tree.x - it.x } ?: tree.x
                val right = trees.filter { it.y == tree.y && it.x > tree.x }
                    .firstOrNull { it.height >= tree.height }
                    ?.let { it.x - tree.x } ?: (maxX - tree.x)
                val up = treesReversed.filter { it.y < tree.y && it.x == tree.x }
                    .firstOrNull { it.height >= tree.height }
                    ?.let { tree.y - it.y } ?: tree.y
                val down = trees.filter { it.y > tree.y && it.x == tree.x }
                    .firstOrNull { it.height >= tree.height }
                    ?.let { it.y - tree.y } ?: (maxY - tree.y)

                left * right * up * down
            }

        score shouldBe 259308
    }
})

private fun getTrees() = getPuzzleInput("day08-input.txt")
    .flatMapIndexed { y, it ->
        it.mapIndexed { x, c -> Tree(x, y, c.digitToInt()) }
    }
    .toList()
