import Util.indexOfClosingBracket
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18 {

    class Number(var left: Number?, var right: Number?, var regular: Int?, var parent: Number? = null) {
        override fun toString() = regular?.toString() ?: "[${left},${right}]"

        operator fun plus(element: Number) = Number(this.copy(), element.copy(), null)
            .also {
                this.parent = it
                element.parent = it
            }

        private fun copy(): Number = Number(left?.copy(), right?.copy(), regular)
            .also {
                it.left?.parent = it
                it.right?.parent = it
            }

        fun reduce(): Number {
            while (hasExplodable() || hasSplitable()) {
                while (hasExplodable()) {
                    this.singleExplode()
                }

                if (hasSplitable())
                    this.singleSplit()
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
                            it.index == regulars.withIndex().first { i -> i.value.second.parent == exploding }.index.dec()
                        }
                        ?.let {
                            it.value.second.regular = it.value.second.regular!! + exploding.left!!.regular!!
                        }

                        // find regular and eventually add to regular to the right
                        regulars.withIndex().singleOrNull {
                            it.index == regulars.withIndex().last { i -> i.value.second.parent == exploding }.index.inc()
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
                number.right = Number(null, null, (regular.inc()) / 2, number)
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

        companion object {
            fun from(input: String, parent: Number? = null): Number =
                input.substring(1, input.length.dec())
                    .let {
                        val number = Number(null, null, null, parent)

                        val end: Int
                        number.left = if (it.first() == '[') {
                            end = it.indexOfClosingBracket()
                            from(it.substring(0, end), number)
                        } else {
                            end = it.indexOf(',')
                            Number(null, null, it.substring(0, end).toInt(), number)
                        }

                        val remaining = it.substring(end.inc())
                        number.right = if (remaining.first() == '[')
                            from(remaining, number)
                        else
                            Number(null, null, remaining.toInt(), number)

                        number
                    }
        }
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
            .map { Number.from(it) }

        val magnitude = numbers
            .flatMap { a ->
                numbers
                    .filterNot { b -> a == b }
                    .map { b ->
                        (a + b).reduce().magnitude()
                    }
            }.maxOrNull()

        assertEquals(4690, magnitude)
    }
}