import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24 {
    enum class BoxType { W, X, Y, Z }
    enum class Instruction { INP, ADD, MUL, DIV, MOD, EQL }

    data class Statement(
        val ins: Instruction,
        val p1: BoxType,
        val p2: BoxType? = null,
        val v2: Long? = null
    )

    data class Box(var value: Long = 0)

    data class Variables(
        val w: Box = Box(),
        val x: Box = Box(),
        val y: Box = Box(),
        val z: Box = Box()
    ) {
        fun get(type: BoxType): Box = when (type) {
            BoxType.W -> w
            BoxType.X -> x
            BoxType.Y -> y
            BoxType.Z -> z
        }
    }

    private val serialSize = 14

    @Test
    @Ignore("it's way too slow...")
    fun `run part 01`() {
        val statementChunks = getStatements()
            .let {
                it.chunked(it.size / serialSize)
            }

        val serials = statementChunks.calculateSerials(1L..9)

        val maxSerial = serials[serials.size - 1]
            ?.filter { z -> z.key == 0L }
            ?.values
            ?.minOfOrNull {
                it.fold("") { acc, w ->
                    acc + w.toString()
                }.toLong()
            }

        assertEquals(69914999975369, maxSerial)
    }

    @Test
    @Ignore("It's also way too slow...")
    fun `run part 02`() {
        val statementChunks = getStatements()
            .let {
                it.chunked(it.size / serialSize)
            }

        val serials = statementChunks.calculateSerials((9L downTo 1))

        val minSerial = serials[serials.size - 1]
            ?.filter { z -> z.key == 0L }
            ?.values
            ?.minOfOrNull {
                it.fold("") { acc, w ->
                    acc + w.toString()
                }.toLong()
            }

        assertEquals(14911675311114, minSerial)
    }

    private fun List<List<Statement>>.calculateSerials(serialDigits: LongProgression) =
        mutableMapOf(0 to mutableMapOf(0L to listOf(0L)))
            .let {
                this
                    .forEachIndexed { index, statements ->
                        it[index + 1] = mutableMapOf()
                        serialDigits
                            .forEach { input ->
                                it[index]!!.forEach { z ->
                                    val vars = statements.execute(listOf(input), z.key)
                                    it[index + 1]!![vars.z.value] = z.value + input
                                }
                            }
                    }
                it
            }

    private fun List<Statement>.execute(input: List<Long>, z: Long = 0) = Variables(z = Box(z))
        .let { vars ->
            var index = 0
            this.forEach { s ->
                when (s.ins) {
                    Instruction.INP ->
                        vars.get(s.p1).value = input[index].also { index++ }
                    Instruction.ADD ->
                        vars.get(s.p1).value = vars.get(s.p1).value + (s.p2?.let { vars.get(it).value } ?: s.v2!!)
                    Instruction.MUL ->
                        vars.get(s.p1).value = vars.get(s.p1).value * (s.p2?.let { vars.get(it).value } ?: s.v2!!)
                    Instruction.DIV ->
                        vars.get(s.p1).value = vars.get(s.p1).value / (s.p2?.let { vars.get(it).value } ?: s.v2!!)
                    Instruction.MOD ->
                        vars.get(s.p1).value = vars.get(s.p1).value % (s.p2?.let { vars.get(it).value } ?: s.v2!!)
                    Instruction.EQL ->
                        vars.get(s.p1).value =
                            if (vars.get(s.p1).value == (s.p2?.let { vars.get(it).value } ?: s.v2!!)) 1
                            else 0
                }
            }
            vars
        }

    private fun getStatements() = Util.getInputAsListOfString("day24-input.txt")
        .map { it.split(" ") }
        .map {
            Statement(
                Instruction.valueOf(it[0].uppercase()),
                BoxType.valueOf(it[1].uppercase()),
                if (it.size == 2) null
                else if (it[2].toIntOrNull() == null) BoxType.valueOf(it[2].uppercase())
                else null,
                if (it.size == 2) null
                else if (it[2].toIntOrNull() != null) it[2].toLong()
                else null
            )
        }
}
