import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07 : StringSpec({
    "puzzle part 01" {
        val sumOfAtMost10K = getDirectorySizes()
            .filterValues { it <= 100000 }
            .values.sum()

        sumOfAtMost10K shouldBe 1778099
    }

    "puzzle part 02" {
        val sizes = getDirectorySizes()

        val needed = 30000000 - 70000000L + sizes.getValue("/")
        val sizeOfCandidate = sizes
            .filterValues { it >= needed }
            .values.min()

        sizeOfCandidate shouldBe 1623571
    }
})

private fun getDirectorySizes(): Map<String, Long> {
    val currentPath = ArrayDeque<String>(1)
    val sizes = getPuzzleInput("day07-input.txt")
        .mapNotNull {
            when {
                it.startsWith("$ ls") -> null
                it.startsWith("$ cd ..") -> null.also { currentPath.removeLast() }
                it.startsWith("$ cd") -> null.also { _ -> currentPath.add(it.dirName()) }
                it.startsWith("dir") -> currentPath.toArray().plus(it.dirName()).join() to 0L
                else -> currentPath.toArray().join() to it.fileSize()
            }
        }
        .groupingBy { (path, _) -> path }
        .fold(0L) { acc, (_, size) -> acc + size }

    return sizes
        .map { (path, size) ->
            path to sizes
                .filterKeys { it != path }
                .map { if (it.key.startsWith(path)) it.value else 0 }
                .sum().plus(size)
        }
        .toMap()
}

private fun String.dirName() = drop(4).trim()
private fun String.fileSize() = split(" ").first().toLong()
private fun Array<Any?>.join() = joinToString("/").replace("//", "/")
