class IntCodeComputer(private var mem: Array<Int>, private val phase: Int? = null) {
    private enum class Mode { POSITION, IMMEDIATE }
    private enum class Op(val code: Int) { ADD(1), MUL(2), IN(3), OUT(4), JNZ(5), JZ(6), SETL(7), SETE(8), ESC(99) }
    private class Instruction(val op: Op, val param1Mode: Mode, val param2Mode: Mode)

    private var phaseSet = false
    private var idx = 0

    var noun
        get() = mem[1]
        set(value) { mem[1] = value}

    var verb
        get() = mem[2]
        set(value) { mem[2] = value}

    val positionZero get() = mem.first()

    fun run(input: Int = 0): Int {
        var output = 0
        while (true) {
            val instr = mem[idx].toInstruction()
            when (instr.op) {
                Op.ESC -> break
                Op.OUT -> output = mem[mem[idx + 1]]
                Op.IN  -> mem[mem[idx + 1]] = if (phase != null && !phaseSet) phase.also { phaseSet = true } else input
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

    private fun Int.toInstruction() = toString()
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