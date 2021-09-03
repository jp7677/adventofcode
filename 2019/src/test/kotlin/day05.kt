import kotlin.test.Test
import kotlin.test.assertEquals

class Day05 {
    enum class Mode { POSITION, IMMEDIATE }
    data class Instruction(
        val opCode: Int,
        val param1Mode: Mode,
        val param2Mode: Mode,
        val param3Mode: Mode,
        val numberOfParams: Int
    )

    @Test
    fun runPart01() {
        val memory = Util.getInputAsListOfInt("day05-input.txt", ",")
            .toTypedArray()

        val input = 1
        var output = 0

        var index = 0
        while (true) {
            val instr = memory[index].toInstruction()
            when(instr.opCode) {
                1 -> memory[memory[index + 3]] = operateAdjacentParams(instr.param1Mode, instr.param2Mode, memory, index, add)
                2 -> memory[memory[index + 3]] = operateAdjacentParams(instr.param1Mode, instr.param2Mode, memory, index, multiply)
                3 -> memory[memory[index + 1]] = input
                4 -> output = getParamValue(instr.param1Mode, memory, index + 1)
                99 -> break
            }
            index += instr.numberOfParams
        }

        assertEquals (4601506, output)
    }

    private fun operateAdjacentParams(param1Mode: Mode, param2Mode: Mode, memory: Array<Int>, index: Int, op: (Int, Int) -> Int): Int =
        op(getParamValue(param1Mode, memory, index + 1), getParamValue(param2Mode, memory, index + 2))

    private fun getParamValue(paramMode: Mode, memory: Array<Int>, index: Int) =
        if (paramMode == Mode.POSITION) memory[memory[index]] else memory[index]

    private fun Int.toInstruction(): Instruction {
        val digits = this.toString()
            .padStart(5, '0')
            .map { it.toString().toInt() }

        val opCode = "${digits[3]}${digits[4]}".toInt()
        return Instruction(
            opCode,
            if (digits[2] == 0) Mode.POSITION else Mode.IMMEDIATE,
            if (digits[1] == 0) Mode.POSITION else Mode.IMMEDIATE,
            if (digits[0] == 0) Mode.POSITION else Mode.IMMEDIATE,
            if ((1..2).contains(opCode)) 4 else 2
        )
    }

    private val add: Int.(Int) -> Int = Int::plus
    private val multiply: Int.(Int) -> Int = Int::times
}