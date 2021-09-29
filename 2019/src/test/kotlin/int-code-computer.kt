import kotlin.IllegalStateException

class IntCodeComputer(private var mem: LongArray, private val phase: Long? = null) {
    private enum class Op(val code: Int) {
        ADD(1), MUL(2), IN(3), OUT(4), JNZ(5), JZ(6), SETL(7), SETE(8), STD(9), ESC(99)
    }
    private enum class Mode(val code: Int) { POSITION(0), IMMEDIATE(1), RELATIVE(2) }
    private class Instruction(val op: Op, val param1Mode: Mode, val param2Mode: Mode, val param3Mode: Mode)
    private var idx = 0
    private var relativeBase = 0L
    private var phaseSet = false

    var noun get() = mem[1]; set(value) { mem[1] = value }
    var verb get() = mem[2]; set(value) { mem[2] = value }
    val positionZero get() = mem.first()

    fun runToCompletion(input: Long = 0) = generateSequence(input) { run(it) }
        .toList().drop(1) // drop seed

    fun run(input: Long = 0): Long? {
        while (true) {
            val instr = readInstruction()
            when (instr.op) {
                Op.ESC -> return null
                else -> {
                    val param1Idx = readParamIndex(instr.param1Mode)
                    when (instr.op) {
                        Op.OUT -> return mem[param1Idx]
                        Op.STD -> relativeBase += mem[param1Idx]
                        Op.IN  -> mem[param1Idx] = if (phase != null && !phaseSet) phase.also { phaseSet = true } else input
                        else -> {
                            val param2Idx = readParamIndex(instr.param2Mode)
                            when (instr.op) {
                                Op.JNZ -> if (mem[param1Idx] != 0L) idx = mem[param2Idx].toInt()
                                Op.JZ  -> if (mem[param1Idx] == 0L) idx = mem[param2Idx].toInt()
                                else -> {
                                    val param3Idx = readParamIndex(instr.param3Mode)
                                    when (instr.op) {
                                        Op.ADD  -> mem[param3Idx] = mem[param1Idx] + mem[param2Idx]
                                        Op.MUL  -> mem[param3Idx] = mem[param1Idx] * mem[param2Idx]
                                        Op.SETL -> mem[param3Idx] = if (mem[param1Idx] < mem[param2Idx]) 1 else 0
                                        Op.SETE -> mem[param3Idx] = if (mem[param1Idx] == mem[param2Idx]) 1 else 0
                                        else -> throw IllegalStateException()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun readInstruction() = mem[idx]
        .toString()
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
        .also { idx++ }

    private fun readParamIndex(mode: Mode) =
        when (mode) {
            Mode.POSITION  -> mem[idx].toInt()
            Mode.IMMEDIATE -> idx
            Mode.RELATIVE  -> (mem[idx] + relativeBase).toInt()
        }
        .also { if (it >= mem.size) mem += LongArray(it - mem.size + 1) }
        .also { idx++ }
}