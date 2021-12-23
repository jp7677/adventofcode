import kotlin.test.Test
import kotlin.test.assertEquals

class Day16 {

    @Test
    fun `run part 01`() {
        val bits = getTransmissionBits()

        val versions = getPacket(bits).first

        assertEquals(960, versions)
    }

    @Test
    fun `run part 02`() {
        val bits = getTransmissionBits()

        val value = getPacket(bits).second

        assertEquals(12301926782560, value)
    }

    private fun getPacket(bits: String, idx: Int = 0): Triple<Long, Long, Int> {
        var idx1 = idx

        var packetVersion = getPacketVersion(bits, idx1).also { idx1 += it.second }.first
        val packetLabel = getPacketType(bits, idx1).also { idx1 += it.second }.first
        val value = if (packetLabel != 4)
            execOperation(bits, idx1, packetLabel)
                .also {
                    packetVersion += it.second
                    idx1 += it.third
                }
                .first
        else
            getLiteral(bits, idx1).also { idx1 += it.second }.first

        return Triple(packetVersion, value, idx1 - idx)
    }

    private fun execOperation(bits: String, idx: Int, op: Int): Triple<Long, Long, Int> {
        var idx1 = idx
        val (lengthIsNumberOfBits, length, _) = getLength(bits, idx1).also { idx1 += it.third }

        val subPackets = mutableListOf<Pair<Long, Long>>()
        if (lengthIsNumberOfBits) {
            val end = idx1 + length
            while (idx1 < end)
                subPackets += getPacket(bits, idx1).also { idx1 += it.third }
                    .let { it.first to it.second }
        } else
            repeat(length) { _ ->
                subPackets += getPacket(bits, idx1).also { idx1 += it.third }
                    .let { it.first to it.second }
            }

        val value = when (op) {
            0 -> subPackets.sumOf { it.second }
            1 -> subPackets.fold(1L) { acc, it -> acc * it.second }
            2 -> subPackets.minOf { it.second }
            3 -> subPackets.maxOf { it.second }
            5 -> if (subPackets.first().second > subPackets.last().second) 1 else 0
            6 -> if (subPackets.first().second < subPackets.last().second) 1 else 0
            7 -> if (subPackets.first().second == subPackets.last().second) 1 else 0
            else -> throw IllegalStateException()
        }

        return Triple(value, subPackets.sumOf { it.first }, idx1 - idx)
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

    private fun getLiteralGroup(bits: String, idx: Int) =
        bits.substring(idx until idx + 5)
            .let {
                Triple(it[0] == '1', it.substring(1), 5)
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