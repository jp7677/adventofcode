import java.lang.IllegalStateException
import java.util.Stack

object Util {

    fun getInputAsString(name: String) =
        Util::class.java.getResource(name)?.readText()
            ?: throw Exception("Invalid input name")

    fun getInputAsListOfString(name: String, separator: String = System.lineSeparator()) =
        getInputAsString(name).split(separator)

    fun getInputAsListOfInt(name: String, separator: String = System.lineSeparator()) =
        getInputAsListOfString(name, separator).map(String::toInt)

    fun getInputAsListOfLong(name: String, separator: String = System.lineSeparator()) =
        getInputAsListOfString(name, separator).map(String::toLong)

    // Based on https://stackoverflow.com/a/52986053
    infix fun Int.towards(to: Int) = IntProgression.fromClosedRange(this, to, if (this > to) -1 else 1)

    // Based on https://stackoverflow.com/a/45764969
    fun String.indexOfClosingBracket(): Int {
        fun Char.matchesBracket(peek: Char) =
            (this == '(' && peek == ')') ||
                (this == ')' && peek == '(') ||
                (this == '[' && peek == ']') ||
                (this == ']' && peek == '[') ||
                (this == '{' && peek == '}') ||
                (this == '}' && peek == '{') ||
                (this == '<' && peek == '>') ||
                (this == '>' && peek == '<')

        val stack = Stack<Char>()
        this.withIndex()
            .filter { (_, it) -> it in listOf('(', ')', '[', ']', '{', '}', '<', '>') }
            .forEach { (index, it) ->
                if (stack.isNotEmpty() && it.matchesBracket(stack.peek()))
                    stack.pop()
                else
                    stack.push(it)

                if (stack.isEmpty())
                    return index.inc()
            }

        throw IllegalStateException()
    }

    // Based on https://stackoverflow.com/a/65145401
    inline fun <T> List<T>.splitWhen(predicate: (T) -> Boolean): List<List<T>> =
        foldIndexed(mutableListOf<MutableList<T>>()) { index, list, element ->
            when {
                predicate(element) -> if (index < size.dec() && !predicate(get(index.inc()))) list.add(mutableListOf())
                list.isNotEmpty() -> list.last().add(element)
                else -> list.add(mutableListOf(element))
            }
            list
        }
}
