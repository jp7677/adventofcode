import java.util.Stack

fun getPuzzleInput(name: String, separator: String = eol) =
    object {}::class.java.getResourceAsStream(name)
        ?.bufferedReader()
        ?.let {
            if (separator == eol)
                it.lineSequence()
            else
                it.readText().split(separator).asSequence()
        }
        ?: throw Exception("Invalid input name")

val eol: String = System.lineSeparator()

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

// Based on https://stackoverflow.com/a/52986053
infix fun Int.towards(to: Int) = IntProgression.fromClosedRange(this, to, if (this > to) -1 else 1)

fun String.indexOfOrNull(char: Char) = indexOf(char).let { if (it == -1) null else it }
