import java.lang.IllegalStateException
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18 {

    class Number(var left: Number?, var right: Number?, var regular: Int?, var parent: Number? = null) {

        companion object {
            fun from(input: String, parent: Number? = null): Number =
                input.substring(1, input.length - 1)
                    .let {
                        val number = Number(null, null, null, parent)

                        val end: Int
                        number.left = if (it.first() == '[') {
                            end = indexOfClosingBracket(it)
                            from(it.substring(0, end), number)
                        } else {
                            end = it.indexOf(',')
                            Number(null, null, it.substring(0, end).toInt(), number)
                        }

                        val remaining = it.substring(end + 1)
                        number.right = if (remaining.first() == '[')
                            from(remaining, number)
                        else
                            Number(null, null, remaining.toInt(), number)

                        number
                    }

            private fun indexOfClosingBracket(inner: String): Int {
                val stack = Stack<Char>()
                for ((index, c) in inner.withIndex()) {
                    if (c !in listOf('[', ']'))
                        continue

                    if (stack.isNotEmpty() && c.matchesBracket(stack.peek()))
                        stack.pop()
                    else
                        stack.push(c)

                    if (stack.isEmpty())
                        return index + 1
                }

                throw IllegalStateException()
            }

            private fun Char.matchesBracket(peek: Char) = (this == '[' && peek == ']') || (this == ']' && peek == '[')
        }

        override fun toString() = regular?.toString() ?: "[${left},${right}]"

        operator fun plus(other: Number) = Number(this, other, null)
            .also {
                this.parent = it
                other.parent = it
            }

        fun reduce(): Number {
            while (hasExplodable() || hasSplitable()) {
                while (hasExplodable()) {
                    this.singleExplode()
                }

                if (hasSplitable()) {
                    this.singleSplit()
                }
            }
            return this
        }

        fun singleExplode() = this.regulars()
            .let { regulars ->
                this.pairs()
                    .firstOrNull { (_, level) -> level >= 5 }
                    ?.let { (exploding, _) ->
                        // find regular and eventually add to regular to the left
                        regulars.withIndex().singleOrNull {
                            it.index == regulars.withIndex().first { i -> i.value.second.parent == exploding }.index - 1
                        }
                        ?.let {
                            it.value.second.regular = it.value.second.regular!! + exploding.left!!.regular!!
                        }
                        // find regular and eventually add to regular to the right
                        regulars.withIndex().singleOrNull {
                            it.index == regulars.withIndex().last { i -> i.value.second.parent == exploding }.index + 1
                        }
                        ?.let {
                            it.value.second.regular = it.value.second.regular!! + exploding.right!!.regular!!
                        }

                        // explode
                        exploding.left = null
                        exploding.right = null
                        exploding.regular = 0
                    }
            }
            .let { this }

        fun singleSplit() = this.regulars()
            .firstOrNull { (regular, _) -> regular >= 10 }
            ?.let { (regular, number) ->
                number.left = Number(null, null, regular / 2, number)
                number.right = Number(null, null, (regular + 1) / 2, number)
                number.regular = null
            }
            .let { this }

        fun magnitude(): Int = regular ?: (left!!.magnitude() * 3 + (right!!.magnitude()) * 2)

        private fun hasExplodable() = this.pairs().any { (_, level) -> level == 5 }

        private fun hasSplitable() = this.regulars().any { (regular, _) -> regular >= 10 }

        private fun pairs(level: Int = 1): List<Pair<Number, Int>> =
            listOf(this to level) +
                (if (left?.regular == null) left!!.pairs(level.inc()) else listOf()) +
                (if (right?.regular == null) right!!.pairs(level.inc()) else listOf())

        private fun regulars(): List<Pair<Int, Number>> =
            if (regular != null) listOf(regular!! to this) else listOf<Pair<Int, Number>>() +
                (left?.regulars() ?: listOf()) +
                (right?.regulars() ?: listOf())
    }

    @Test
    fun `run Number tests`() {
        assertEquals("[1,2]", Number.from("[1,2]").toString())
        assertEquals("[[1,2],3]", Number.from("[[1,2],3]").toString())
        assertEquals("[9,[8,7]]", Number.from("[9,[8,7]]").toString())
        assertEquals("[[1,9],[8,5]]", Number.from("[[1,9],[8,5]]").toString())
        assertEquals("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]", Number.from("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]").toString())
        assertEquals("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]", Number.from("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]").toString())
        assertEquals("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", Number.from("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]").toString())

        assertEquals("[[1,2],[[3,4],5]]", (Number.from("[1,2]") + Number.from("[[3,4],5]")).toString())

        assertEquals("[[[[0,9],2],3],4]", Number.from("[[[[[9,8],1],2],3],4]").singleExplode().toString())
        assertEquals("[7,[6,[5,[7,0]]]]", Number.from("[7,[6,[5,[4,[3,2]]]]]").singleExplode().toString())
        assertEquals("[[6,[5,[7,0]]],3]", Number.from("[[6,[5,[4,[3,2]]]],1]").singleExplode().toString())
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", Number.from("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").singleExplode().toString())
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", Number.from("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").singleExplode().toString())

        assertEquals("[[5,5],1]", Number.from("[10,1]").singleSplit().toString())
        assertEquals("[[5,6],1]", Number.from("[11,1]").singleSplit().toString())
        assertEquals("[[6,6],1]", Number.from("[12,1]").singleSplit().toString())

        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", (Number.from("[[[[4,3],4],4],[7,[[8,4],9]]]") + Number.from("[1,1]")).reduce().toString())

        assertEquals(143, Number.from("[[1,2],[[3,4],5]]").magnitude())
        assertEquals(1384, Number.from("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude())
        assertEquals(445, Number.from("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude())
        assertEquals(791, Number.from("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude())
        assertEquals(1137, Number.from("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude())
        assertEquals(3488, Number.from("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude())
    }

    @Test
    fun `run part 01`() {
        val magnitude = Util.getInputAsListOfString("day18-input.txt")
            .map { Number.from(it) }
            .reduce { it, acc -> (it + acc).reduce() }
            .magnitude()

        assertEquals(2907, magnitude)
    }

    @Test
    fun `run part 02`() {
        val numbers = Util.getInputAsListOfString("day18-input.txt")

        val magnitude = numbers
            .flatMap { a ->
                numbers.mapNotNull { b ->
                    if (a != b) (Number.from(a) + Number.from(b)).reduce().magnitude() else null
                }
            }.maxOrNull()

        assertEquals(4690, magnitude)
    }
}