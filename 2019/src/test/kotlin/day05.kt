import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {
    enum class Mode { POSITION, IMMEDIATE }
    enum class Op(val code: Int) {
        ADD(1), MULTIPLY(2), INPUT(3), OUTPUT(4),
        JUMP_IF_TRUE(5), JUMP_IF_FALSE(6), LESS_THAN(7), EQUALS(8),
        EXIT(99)
    }
    data class Instruction(val op: Op, val param1Mode: Mode, val param2Mode: Mode, val param3Mode: Mode)

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

    private fun runProgram(memory: Array<Int>, input: Int): Int {
        var output = 0
        var index = 0
        while (true) {
            val instr = memory[index].toInstruction()
            when (instr.op) {
                Op.EXIT -> break
                Op.INPUT,
                Op.OUTPUT -> {
                    when (instr.op) {
                        Op.INPUT  -> memory[memory[index + 1]] = input
                        Op.OUTPUT -> output = getParamValue(instr.param1Mode, memory, index + 1)
                        else -> throw IllegalStateException()
                    }
                    index += 2
                }
                else -> {
                    val param1Value = getParamValue(instr.param1Mode, memory, index + 1)
                    val param2Value = getParamValue(instr.param2Mode, memory, index + 2)
                    when (instr.op) {
                        Op.JUMP_IF_TRUE  -> index = if (param1Value != 0) param2Value else index + 3
                        Op.JUMP_IF_FALSE -> index = if (param1Value == 0) param2Value else index + 3
                        else -> {
                            when (instr.op) {
                                Op.ADD       -> memory[memory[index + 3]] = param1Value + param2Value
                                Op.MULTIPLY  -> memory[memory[index + 3]] = param1Value * param2Value
                                Op.LESS_THAN -> memory[memory[index + 3]] = if (param1Value < param2Value) 1 else 0
                                Op.EQUALS    -> memory[memory[index + 3]] = if (param1Value == param2Value) 1 else 0
                                else -> throw IllegalStateException()
                            }
                            index += 4
                        }
                    }
                }
            }
        }

        return output
    }

    private fun getParamValue(paramMode: Mode, memory: Array<Int>, index: Int) =
        if (paramMode == Mode.POSITION) memory[memory[index]] else memory[index]

    private fun Int.toInstruction(): Instruction = this.toString()
        .padStart(5, '0')
        .map { it.toString().toInt() }
        .let { digits ->
            Instruction(
                Op.values().single { it.code == "${digits[3]}${digits[4]}".toInt() },
                if (digits[2] == 0) Mode.POSITION else Mode.IMMEDIATE,
                if (digits[1] == 0) Mode.POSITION else Mode.IMMEDIATE,
                if (digits[0] == 0) Mode.POSITION else Mode.IMMEDIATE
            )
        }
}