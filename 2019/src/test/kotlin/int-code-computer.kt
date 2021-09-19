class IntCodeComputer(private var mem: LongArray, private val phase: Long? = null) {
    private enum class Op(val code: Int) {
        ADD(1), MUL(2), IN(3), OUT(4), JNZ(5), JZ(6), SETL(7), SETE(8), STD(9),
        ESC(99)
    }
    private enum class Mode(val code: Int) { POSITION(0), IMMEDIATE(1), RELATIVE(2) }
    private class Instruction(val op: Op, val param1Mode: Mode, val param2Mode: Mode, val param3Mode: Mode)
    private var phaseSet = false
    private var idx = 0
    private var relativeBase = 0L

    var noun get() = mem[1]; set(value) { mem[1] = value }
    var verb get() = mem[2]; set(value) { mem[2] = value }
    val positionZero get() = mem.first()

    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun run(input: Long = 0): Long? {
        var output = 0L
        while (true) {
            val instr = mem[idx].toInstruction()
            when (instr.op) {
                Op.ESC -> return null
                else -> {
                    val param1Idx = getParamIndex(instr.param1Mode, 1)
                    when (instr.op) {
                        Op.IN  -> mem[param1Idx] = if (phase != null && !phaseSet) phase.also { phaseSet = true } else input
                        Op.OUT -> output = mem[param1Idx]
                        Op.STD -> relativeBase += mem[param1Idx]
                        else -> {
                            val param2Index = getParamIndex(instr.param2Mode, 2)
                            when (instr.op) {
                                Op.JNZ -> idx = if (mem[param1Idx] != 0L) mem[param2Index].toInt() else idx + 3
                                Op.JZ  -> idx = if (mem[param1Idx] == 0L) mem[param2Index].toInt() else idx + 3
                                else -> {
                                    val param3Index = getParamIndex(instr.param3Mode, 3)
                                    if (param3Index >= mem.size) mem += LongArray(param3Index - mem.size + 1)
                                    when (instr.op) {
                                        Op.ADD  -> mem[param3Index] = mem[param1Idx] + mem[param2Index]
                                        Op.MUL  -> mem[param3Index] = mem[param1Idx] * mem[param2Index]
                                        Op.SETL -> mem[param3Index] = if (mem[param1Idx] < mem[param2Index]) 1 else 0
                                        Op.SETE -> mem[param3Index] = if (mem[param1Idx] == mem[param2Index]) 1 else 0
                                    }
                                }
                            }
                        }
                    }
                }
            }

            idx += when (instr.op) {
                Op.IN, Op.OUT, Op.STD            -> 2
                Op.ADD, Op.MUL, Op.SETL, Op.SETE -> 4
                else                             -> 0
            }

            if (instr.op == Op.OUT) return output
        }
    }

    fun runUntilExit(input: Long = 0) = generateSequence(input) { run(it) }
        .toList().drop(1)

    private fun Long.toInstruction() = toString()
        .padStart(5, '0')
        .map { it.toString().toInt() }
        .let { digits ->
            Instruction(
                Op.values().single { it.code == "${digits[3]}${digits[4]}".toInt() },
                Mode.values().single { it.code == digits[2] },
                Mode.values().single { it.code == digits[1] },
                Mode.values().single { it.code == digits[0] }
            )
        }

    private fun getParamIndex(mode: Mode, offset: Int) =
        when (mode) {
            Mode.POSITION  -> mem[idx + offset].toInt()
            Mode.IMMEDIATE -> idx + offset
            Mode.RELATIVE  -> (mem[idx + offset] + relativeBase).toInt()
        }
}