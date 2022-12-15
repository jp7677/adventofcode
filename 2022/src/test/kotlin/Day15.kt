import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private data class Position15(val x: Int, val y: Int)
private data class Sensor(val position: Position15, val beacon: Position15) {
    val distanceToBeaon: Int = position.toManhattenDistance(beacon)

    fun knownRange(y: Int): IntRange? {
        val distanceY = (this.position.y - y).absoluteValue
        if (distanceY > distanceToBeaon) return null

        val x1 = this.position.x - (distanceToBeaon - distanceY)
        val x2 = this.position.x + (distanceToBeaon - distanceY)
        return min(x1, x2)..max(x1, x2)
    }
}

class Day15 : StringSpec({
    "puzzle part 01" {
        val sensors = getSensors().toList()

        val row = 2000000
        val knownRangeAtRow = sensors.mapNotNull { it.knownRange(row) }
        val beaconsInRow = sensors.map { it.beacon }.toSet().count { it.y == row }

        val countOfKnownAtRow = (knownRangeAtRow.minOf { it.first } ..knownRangeAtRow.maxOf { it.last })
            .count { y -> knownRangeAtRow.any { y in it } }
        val positionsAtRowWithoutBeacon = countOfKnownAtRow - beaconsInRow

        positionsAtRowWithoutBeacon shouldBe 5832528
    }

    "puzzle part 02" {
        val sensors = getSensors().toList()

        val boundary = 4000000
        val frequency = (0..boundary).firstNotNullOf { y ->
            sensors.mapNotNull { it.knownRange(y) }
                .map { IntRange(max(it.first, 0), min(it.last, boundary)) }
                .sortedBy { it.first }
                .missing()
                ?.let { it * 4000000 + y }
        }

        frequency shouldBe 13360899249595
    }
})

private fun List<IntRange>.missing(): Long? {
    this.reduce { acc, it ->
        if (it.first > acc.last + 1) return acc.last.toLong() + 1
        min(acc.first, it.first)..max(acc.last, it.last)
    }
    return null
}

private fun Position15.toManhattenDistance(other: Position15) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

private val re = Regex("x=(-?\\d+), y=(-?\\d+)")
private fun getSensors() = getPuzzleInput("day15-input.txt").map { line ->
    re.findAll(line).let { m ->
        Sensor(
            Position15(m.first().groupValues[1].toInt(), m.first().groupValues[2].toInt()),
            Position15(m.last().groupValues[1].toInt(), m.last().groupValues[2].toInt()),
        )
    }
}