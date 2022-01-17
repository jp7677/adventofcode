import kotlin.test.Test
import kotlin.test.assertEquals

class Day16 {

    @Test
    fun `run part 01`() {
        val bits = getTransmissionBits()

        val version = getPacket(bits).first

        assertEquals(960, version)
    }

    @Test
    fun `run part 02`() {
        val bits = getTransmissionBits()

        val value = getPacket(bits).second

        assertEquals(12301926782560, value)
    }

    private fun getPacket(bits: String, idx: Int = 0): Triple<Long, Long, Int> {
        var idx1 = idx

        var version = getVersion(bits, idx1).also { idx1 += it.second }.first
        val label = getType(bits, idx1).also { idx1 += it.second }.first
        val value = if (label != 4)
            execOperation(bits, idx1, label)
                .also {
                    version += it.second
                    idx1 += it.third
                }
                .first
        else
            getLiteral(bits, idx1).also { idx1 += it.second }.first

        return version to value to idx1 - idx
    }

    private fun getVersion(bits: String, idx: Int) = bits
        .substring(idx until idx + 3).toLong(2) to 3

    private fun getType(bits: String, idx: Int) = bits
        .substring(idx until idx + 3).toInt(2) to 3

    private fun execOperation(bits: String, idx: Int, op: Int): Triple<Long, Long, Int> {
        var idx1 = idx
        val packets = mutableListOf<Pair<Long, Long>>()

        val (lengthIsNumberOfBits, length, _) = getLength(bits, idx1).also { idx1 += it.third }
        if (lengthIsNumberOfBits) {
            val end = idx1 + length
            while (idx1 < end)
                packets += getPacket(bits, idx1).also { idx1 += it.third }
                    .let { it.first to it.second }
        } else
            repeat(length) { _ ->
                packets += getPacket(bits, idx1).also { idx1 += it.third }
                    .let { it.first to it.second }
            }

        val value = when (op) {
            0 -> packets.sumOf { it.second }
            1 -> packets.fold(1L) { acc, it -> acc * it.second }
            2 -> packets.minOf { it.second }
            3 -> packets.maxOf { it.second }
            5 -> if (packets.first().second > packets.last().second) 1 else 0
            6 -> if (packets.first().second < packets.last().second) 1 else 0
            7 -> if (packets.first().second == packets.last().second) 1 else 0
            else -> throw IllegalStateException()
        }

        return value to packets.sumOf { it.first } to idx1 - idx
    }

    private fun getLength(bits: String, idx: Int): Triple<Boolean, Int, Int> {
        val length = if (bits[idx] == '0') 16 else 12
        return (bits[idx] == '0') to bits.substring(idx.inc() until idx + length).toInt(2) to length
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
                (it[0] == '1') to it.substring(1) to 5
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

    infix fun <X : Pair<A, B>, A, B, C> X.to(that: C) = Triple(this.first, this.second, that)
}
