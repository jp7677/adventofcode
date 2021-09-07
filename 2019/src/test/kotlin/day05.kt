import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {
    enum class Mode { POSITION, IMMEDIATE }
    enum class Op(val code: Int) { ADD(1), MUL(2), IN(3), OUT(4), JNZ(5), JZ(6), SETL(7), SETE(8), ESC(99) }
    data class Instruction(val op: Op, val param1Mode: Mode, val param2Mode: Mode)

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",")
            .toTypedArray()

        val output = runProgram(memory, 1)

        assertEquals (4601506, output)
    }

    @Test
    fun runPart02() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",")
            .toTypedArray()

        val output = runProgram(memory, 5)

        assertEquals (5525561, output)
    }

    private fun runProgram(mem: Array<Int>, input: Int): Int {
        var output = 0
        var idx = 0
        while (true) {
            val instr = mem[idx].toInstruction()
            when (instr.op) {
                Op.ESC  -> break
                Op.OUT  -> output = mem[mem[idx + 1]]
                Op.IN   -> mem[mem[idx + 1]] = input
                else -> {
                    val param1Value = if (instr.param1Mode == Mode.POSITION) mem[mem[idx + 1]] else mem[idx + 1]
                    val param2Value = if (instr.param2Mode == Mode.POSITION) mem[mem[idx + 2]] else mem[idx + 2]
                    @Suppress("NON_EXHAUSTIVE_WHEN")
                    when (instr.op) {
                        Op.ADD  -> mem[mem[idx + 3]] = param1Value + param2Value
                        Op.MUL  -> mem[mem[idx + 3]] = param1Value * param2Value
                        Op.SETL -> mem[mem[idx + 3]] = if (param1Value < param2Value) 1 else 0
                        Op.SETE -> mem[mem[idx + 3]] = if (param1Value == param2Value) 1 else 0
                        Op.JNZ  -> idx = if (param1Value != 0) param2Value else idx + 3
                        Op.JZ   -> idx = if (param1Value == 0) param2Value else idx + 3
                    }
                }
            }

            idx += when (instr.op) {
                Op.ADD, Op.MUL, Op.SETL, Op.SETE -> 4
                Op.IN, Op.OUT                    -> 2
                else                             -> 0
            }
        }

        return output
    }

    private fun Int.toInstruction() = this.toString()
        .padStart(5, '0')
        .map { it.toString().toInt() }
        .let { digits ->
            Instruction(
                Op.values().single { it.code == "${digits[3]}${digits[4]}".toInt() },
                if (digits[2] == 0) Mode.POSITION else Mode.IMMEDIATE,
                if (digits[1] == 0) Mode.POSITION else Mode.IMMEDIATE
            )
        }
}