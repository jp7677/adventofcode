import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07 : StringSpec({
    "puzzle part 01" {
        val sumOfAtMost10K = getDirectorySizes()
            .map { it.value }
            .filter { it <= 100000 }
            .sum()

        sumOfAtMost10K shouldBe 1778099
    }

    "puzzle part 02" {
        val sizes = getDirectorySizes()

        val needed = 30000000 - 70000000L + sizes.getValue("/")
        val sizeOfCandidate = sizes
            .map { it.value }
            .filter { it >= needed }
            .min()

        sizeOfCandidate shouldBe 1623571
    }
})

private fun getDirectorySizes(): Map<String, Long> {
    val currentPath = ArrayDeque<String>(1)
    val sizes = getPuzzleInput("day07-input.txt")
        .mapNotNull {
            if (it.startsWith("$ cd")) {
                if (it.startsWith("$ cd .."))
                    currentPath.removeLast()
                else
                    currentPath.add(it.drop(5))
                null
            } else if (it.startsWith("$ ls"))
                null
            else if (it.startsWith("dir"))
                (currentPath.toArray() + it.drop(4)).join() to 0L
            else
                currentPath.toArray().join() to it.split(" ").first().toLong()
        }
        .groupingBy { (path, _) -> path }
        .fold(0L) { acc, (_, size) -> acc + size }

    return sizes
        .map { (path, size) ->
            path to (sizes
                .filterNot { it.key == path }
                .map { if (it.key.startsWith(path)) it.value else 0 }
                .sum() + size)
        }.toMap()
}

private fun Array<Any?>.join() = joinToString("/").replace("//", "/")