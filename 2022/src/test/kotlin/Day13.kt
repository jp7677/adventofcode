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
            if (packet == "[]") return Packet(listOf(Packet()))
            packet.toIntOrNull()?.let { return Packet(it) }

            return generateSequence(1) { start ->
                if (start >= packet.length) null
                else {
                    val part = packet.drop(start)
                    val length = if (packet[start] == '[')
                        part.indexOfClosingBracket()
                    else
                        part.indexOfOrNull(',') ?: part.length.dec()

                    start + length + 1
                }
            }
                .toList()
                .zipWithNext()
                .map { (start, end) -> packet.drop(start).take(end - start - 1) }
                .map { token -> parse(token) }
                .let { Packet(it) }
        }

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
