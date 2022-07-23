import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22 {
    data class Cuboid(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val minZ: Int, val maxZ: Int) {
        val initialization get() = minX >= -50 && maxX <= 50 && minY >= -50 && maxY <= 50 && minZ >= -50 && maxZ <= 50

        fun size() = abs(maxX - minX + 1L) * abs(maxY - minY + 1L) * abs(maxZ - minZ + 1L)

        fun normalize() = Cuboid(
            if (minX < maxX) minX else maxX,
            if (minX < maxX) maxX else minX,
            if (minY < maxY) minY else maxY,
            if (minY < maxY) maxY else minY,
            if (minZ < maxZ) minZ else maxZ,
            if (minZ < maxZ) maxZ else minZ
        )

        infix fun intersect(other: Cuboid): Cuboid? {
            if (minX > other.maxX || maxX < other.minX ||
                minY > other.maxY || maxY < other.minY ||
                minZ > other.maxZ || maxZ < other.minZ
            )
                return null

            return Cuboid(
                if (minX > other.minX) minX else other.minX,
                if (maxX < other.maxX) maxX else other.maxX,
                if (minY > other.minY) minY else other.minY,
                if (maxY < other.maxY) maxY else other.maxY,
                if (minZ > other.minZ) minZ else other.minZ,
                if (maxZ < other.maxZ) maxZ else other.maxZ
            )
        }
    }
    data class Step(val on: Boolean, val cuboid: Cuboid)

    @Test
    fun `run part 01`() {
        val steps = getSteps()
            .filter { it.cuboid.initialization }

        val count = runSteps(steps)

        assertEquals(653798, count)
    }

    @Test
    fun `run part 02`() {
        val steps = getSteps()

        val count = runSteps(steps)

        assertEquals(1257350313518866, count)
    }

    private fun runSteps(steps: List<Step>) = steps.mapIndexed { index, step ->
        val previouslyTurnedOn = steps
            .take(index)
            .filter { it.on }
            .mapNotNull { step.cuboid intersect it.cuboid }

        if (step.on) {
            step.cuboid.size() - previouslyTurnedOn.calcEffectiveSize()
        } else {
            val turnOnLater = steps
                .drop(index + 1)
                .map { it.cuboid }
                .mapNotNull { step.cuboid intersect it }

            previouslyTurnedOn.except(turnOnLater) * -1
        }
    }
        .sum()

    private fun List<Cuboid>.except(turnOnLaterSize: List<Cuboid>): Long = this
        .calcEffectiveSize() -
        this
            .flatMap { cuboid ->
                turnOnLaterSize.mapNotNull { cuboid intersect it }
            }
            .calcEffectiveSize()

    private fun List<Cuboid>.calcEffectiveSize(): Long = this
        .mapIndexed { index, it ->
            it.size() - this
                .take(index)
                .mapNotNull { i -> it intersect i }
                .calcEffectiveSize()
        }
        .sum()

    private fun getSteps() = Util.getInputAsListOfString("day22-input.txt")
        .map {
            val s = it.split(' ')
            val r = s.last().split("=", "..", ",")
            Step(
                s.first() == "on",
                Cuboid(r[1].toInt(), r[2].toInt(), r[4].toInt(), r[5].toInt(), r[7].toInt(), r[8].toInt()).normalize()
            )
        }
}
