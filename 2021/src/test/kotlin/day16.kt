import kotlin.test.Test
import kotlin.test.assertEquals

class Day16 {

    @Test
    fun `run part 01`() {
        val bits = getTransmissionBits()

        val versions = getPacket(bits).first

        assertEquals(960, versions)
    }

    private fun getPacket(bits: String, idx: Int = 0): Pair<Long, Int> {
        var idx1 = idx

        var packetVersion = getPacketVersion(bits, idx1).also { idx1 += it.second }.first
        val packetLabel = getPacketType(bits, idx1).also { idx1 += it.second }.first

        when (packetLabel) {
            4 -> {  // Literal
                val literal = getLiteral(bits, idx1).also { idx1 += it.second }.first
            }
            else -> { // Operator
                val (lengthIsNumberOfBits, length) = getLength(bits, idx1).also { idx1 += it.third }
                    .let{ it.first to it.second }

                if (lengthIsNumberOfBits) {
                    val end = idx1 + length
                    while (idx1 < end)
                        packetVersion += getPacket(bits, idx1).also { idx1 += it.second }.first
                }
                else
                    repeat(length) { _ ->
                        packetVersion += getPacket(bits, idx1).also { idx1 += it.second }.first
                    }
            }
        }

        return packetVersion to (idx1 - idx)
    }

    private fun getPacketVersion(bits: String, idx: Int) = bits
        .substring(idx until idx + 3).toLong(2) to 3

    private fun getPacketType(bits: String, idx: Int) = bits
        .substring(idx until idx + 3).toInt(2) to 3

    private fun getLength(bits: String, idx: Int): Triple<Boolean, Int, Int> {
        val length = if (bits[idx] == '0') 16 else 12
        return Triple(
            bits[idx] == '0',
            bits.substring(idx + 1 until idx + length).toInt(2),
            length
        )
    }

    private fun getLiteral(bits: String, idx: Int): Pair<Long, Int> {
        var idx1 = idx
        var literal = ""
        var hasNext = true

        while (hasNext)
            hasNext = getLiteralGroup(bits, idx1)
                .also {
                    literal += it.second
                    idx1 += it.third
                }
                .first

        return literal.toLong(2) to idx1 - idx
    }

    private fun getLiteralGroup(bits: String, idx: Int): Triple<Boolean, String, Int> {
        val group = bits.substring(idx until idx + 5)
        return Triple(group[0] == '1', group.substring(1), 5)
    }

    private fun getTransmissionBits() = Util.getInputAsString("day16-input.txt")
        .toList()
        .joinToString("") {
            it
                .toString()
                .toInt(16)
                .toString(2)
                .padStart(4, '0')
        }
}