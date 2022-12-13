import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.min

private data class Packet(val integer: Int?, val packets: List<Packet>?) : Comparable<Packet> {
    constructor() : this (null, null)
    constructor(integer: Int) : this (integer, null)
    constructor(packets: List<Packet>) : this (null, packets)

    companion object {
        fun parse(packet: String): Packet {
            if (packet.isEmpty()) return Packet()
            if (!packet.startsWith('[') || !packet.endsWith(']')) throw IllegalArgumentException()

            return packet
                .drop(1).dropLast(1)
                .maskLists()
                .split(',')
                .map { it.toIntOrNull()?.let { integer -> Packet(integer) } ?: parse(it.unmask()) }
                .let { Packet(it) }
        }

        private fun String.maskLists(): String {
            var s = this
            while (s.contains('[')) {
                val start = s.indexOf('[')
                val end = start + s.drop(start).indexOfClosingBracket()
                val maskedList = s.drop(start).take(end - start).mask()
                s = s.take(start) + maskedList + s.drop(end)
            }
            return s
        }

        private fun String.mask() = this.replace('[', '(').replace(']', ')').replace(',', '.')
        private fun String.unmask() = this.replace('(', '[').replace(')', ']').replace('.', ',')

        fun wrap(integer: Int) = Packet(listOf(Packet(integer)))
    }

    override operator fun compareTo(other: Packet): Int {
        if (isValue() && other.isContainer()) return wrap(integer!!).compareTo(other)
        if (isContainer() && other.isValue()) return this.compareTo(wrap(other.integer!!))
        if (isValue() && other.isValue()) return integer!!.compareTo(other.integer!!)
        if (!isEmpty() && other.isEmpty()) return 1
        if (isEmpty() && !other.isEmpty()) return -1

        if (isContainer() && other.isContainer()) {
            (0 until min(packets!!.size, other.packets!!.size)).forEach {
                if (other.packets[it] < packets[it]) return 1
                if (packets[it] < other.packets[it]) return -1
            }
            if (packets.size != other.packets.size) return packets.size.compareTo(other.packets.size)
        }

        return 0
    }

    private fun isEmpty() = integer == null && packets == null
    private fun isContainer() = integer == null && packets != null
    private fun isValue() = integer != null && packets == null
}

class Day13 : StringSpec({
    "puzzle part 01" {
        val indicesSum = getPuzzleInput("day13-input.txt", "$eol$eol")
            .map { pair ->
                pair.split(eol).let { Packet.parse(it.first()) to Packet.parse(it.last()) }
            }
            .toList().withIndex()
            .map {
                if (it.value.first < it.value.second) it.index + 1 else 0
            }

        indicesSum.sum() shouldBe 5623
    }

    "puzzle part 02" {
        val packet1 = Packet.parse("[[2]]")
        val packet2 = Packet.parse("[[6]]")
        val sortedPackets = getPuzzleInput("day13-input.txt")
            .mapNotNull { if (it.isNotBlank()) Packet.parse(it) else null }
            .plus(listOf(packet1, packet2))
            .toList()
            .sorted().withIndex()

        val key1 = sortedPackets.single { it.value == packet1 }.index + 1
        val key2 = sortedPackets.single { it.value == packet2 }.index + 1

        key1 * key2 shouldBe 20570
    }
})
