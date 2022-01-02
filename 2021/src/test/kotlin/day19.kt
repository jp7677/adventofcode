import Util.splitWhen
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19 {
    data class Position(val x: Int, val y: Int, val z: Int)
    data class Scanner(val index: Int, val beacons: Set<Position>)
    enum class Turn {
        /** negative Z */ NONE,
        /** positive X */ RIGHT,
        /** negative X */ LEFT,
        /** positive Y */ UP,
        /** negative Y */ DOWN,
        /** positive Z */ BACKWARDS
    }
    enum class Rotate { NONE, CLOCKWISE, COUNTERCLOCKWISE, UPSIDE_DOWN }
    data class Correction(val turn: Turn, val rotation: Rotate, val x: Int, val y: Int, val z: Int)
    private val turns = listOf(Turn.NONE, Turn.RIGHT, Turn.LEFT, Turn.UP, Turn.DOWN, Turn.BACKWARDS)
    private val rotations = listOf(Rotate.NONE, Rotate.CLOCKWISE, Rotate.COUNTERCLOCKWISE, Rotate.UPSIDE_DOWN)

    @Test
    fun `run part 01`() {
        val scanners = getScanners()

        val (_, detectedBeacons) = getOceanMap(scanners)
        val count = detectedBeacons.count()

        assertEquals(451, count)
    }

    @Test
    fun `run part 02`() {
        val scanners = getScanners()

        val (detectedScanners, _) = getOceanMap(scanners)
        val maxDistance = detectedScanners
            .flatMap { a ->
                detectedScanners
                    .filterNot { b -> a == b }
                    .map { b ->
                        a.second.distance(b.second)
                    }
            }
            .maxOrNull()

        assertEquals(13184, maxDistance)
    }

    private fun getOceanMap(scanners: List<Scanner>): Pair<MutableList<Pair<Int, Position>>, MutableSet<Position>> {
        val detectedScanners = mutableListOf(scanners.first().index to Position(0, 0, 0))
        val detectedBeacons = scanners.first().beacons.toMutableSet()

        while (detectedScanners.size != scanners.size) {
            val trianglesFromDetectedBeacons = detectedBeacons.getTrianglesWithBeacons()

            scanners
                .filterNot { detectedScanners.map { scanner -> scanner.first }.contains(it.index) }
                .map {
                    it to it.beacons.getTrianglesWithBeacons().filter { (triangles, _) ->
                        triangles.sorted() in trianglesFromDetectedBeacons
                            .map { (triangle, _) -> triangle.sorted() }
                    }
                }
                .filter { (_, trianglesWithBeacons) -> trianglesWithBeacons.count() * 3 >= 12 }
                .forEach { (scanner, trianglesWithBeacons) ->
                    trianglesWithBeacons
                        .map { (triangle, beacons) ->
                            val (_, matchingDetectedBeacons) = trianglesFromDetectedBeacons
                                .single { (detectedTriangle, _) -> triangle == detectedTriangle }

                            turns.flatMap { turn ->
                                rotations.mapNotNull { rotation ->
                                    val faced = beacons[0].face(turn, rotation)
                                    val distance1 = matchingDetectedBeacons[0].distance(faced)
                                    val distance2 = matchingDetectedBeacons[1].distance(beacons[1].face(turn, rotation))
                                    val distance3 = matchingDetectedBeacons[2].distance(beacons[2].face(turn, rotation))
                                    if (distance1 == distance2 && distance1 == distance3) {
                                        Correction(
                                            turn,
                                            rotation,
                                            faced.x - matchingDetectedBeacons[0].x,
                                            faced.y - matchingDetectedBeacons[0].y,
                                            faced.z - matchingDetectedBeacons[0].z
                                        )
                                    } else null
                                }
                            }.toSet()
                        }
                        .let {
                            val correction = it
                                .filterNot { corrections -> corrections.isEmpty() }
                                .reduce { acc, corrections -> acc intersect corrections }
                                .let { corrections ->
                                    if (corrections.size == 1) corrections.first() else throw IllegalStateException()
                                }

                            detectedScanners.add(scanner.index to Position(correction.x, correction.y, correction.z))
                            scanner.beacons
                                .map { beacon -> beacon.face(correction.turn, correction.rotation) }
                                .forEach { beacon ->
                                    detectedBeacons.add(
                                        Position(
                                            beacon.x - correction.x,
                                            beacon.y - correction.y,
                                            beacon.z - correction.z
                                        )
                                    )
                                }
                        }
                }
        }
        return Pair(detectedScanners, detectedBeacons)
    }

    private fun Set<Position>.getTrianglesWithBeacons() =
        this.map {
            this
                .filterNot { beacon -> beacon == it }
                .sortedBy { beacon -> beacon.distance(it) }
                .take(2)
                .let { beacons ->
                    listOf(
                        it.distance(beacons.first()),
                        it.distance(beacons.last()),
                        beacons.first().distance(beacons.last())
                    ) to listOf(it, beacons.first(), beacons.last())
                }
        }
            .sortedWith(compareBy<Pair<List<Int>, List<Position>>> { (triangle, _) -> triangle[0] }
                .thenBy { (triangle, _) -> triangle[1] } )
            .distinctBy { (triangle, _) -> triangle.sorted() }

    private fun Position.face(turn: Turn, rotation: Rotate) = when (turn to rotation) {
        Turn.NONE to Rotate.NONE                  -> this
        Turn.NONE to Rotate.CLOCKWISE             -> Position(this.y,      this.x * -1, this.z)
        Turn.NONE to Rotate.COUNTERCLOCKWISE      -> Position(this.y * -1, this.x,      this.z)
        Turn.NONE to Rotate.UPSIDE_DOWN           -> Position(this.x * -1, this.y * -1, this.z)
        Turn.RIGHT to Rotate.NONE                 -> Position(this.z,      this.y,      this.x * -1)
        Turn.RIGHT to Rotate.CLOCKWISE            -> Position(this.z,      this.x * -1, this.y * -1)
        Turn.RIGHT to Rotate.COUNTERCLOCKWISE     -> Position(this.z,      this.x,      this.y)
        Turn.RIGHT to Rotate.UPSIDE_DOWN          -> Position(this.z,      this.y * -1, this.x)
        Turn.LEFT to Rotate.NONE                  -> Position(this.z * -1, this.y,      this.x)
        Turn.LEFT to Rotate.CLOCKWISE             -> Position(this.z * -1, this.x * -1, this.y)
        Turn.LEFT to Rotate.COUNTERCLOCKWISE      -> Position(this.z * -1, this.x,      this.y * -1)
        Turn.LEFT to Rotate.UPSIDE_DOWN           -> Position(this.z * -1, this.y * -1, this.x * -1)
        Turn.UP to Rotate.NONE                    -> Position(this.x,      this.z,      this.y * -1)
        Turn.UP to Rotate.CLOCKWISE               -> Position(this.y,      this.z,      this.x)
        Turn.UP to Rotate.COUNTERCLOCKWISE        -> Position(this.y * -1, this.z,      this.x * -1)
        Turn.UP to Rotate.UPSIDE_DOWN             -> Position(this.x * -1, this.z,      this.y)
        Turn.DOWN to Rotate.NONE                  -> Position(this.x,      this.z * -1, this.y)
        Turn.DOWN to Rotate.CLOCKWISE             -> Position(this.y * -1, this.z * -1, this.x)
        Turn.DOWN to Rotate.COUNTERCLOCKWISE      -> Position(this.y,      this.z * -1, this.x * -1)
        Turn.DOWN to Rotate.UPSIDE_DOWN           -> Position(this.x * -1, this.z * -1, this.y * -1)
        Turn.BACKWARDS to Rotate.NONE             -> Position(this.x * -1, this.y,      this.z * -1)
        Turn.BACKWARDS to Rotate.CLOCKWISE        -> Position(this.y,      this.x,      this.z * -1)
        Turn.BACKWARDS to Rotate.COUNTERCLOCKWISE -> Position(this.y * -1, this.x * -1, this.z * -1)
        Turn.BACKWARDS to Rotate.UPSIDE_DOWN      -> Position(this.x,      this.y * -1, this.z * -1)
        else -> throw IllegalStateException()
    }

    private fun Position.distance(element: Position = Position(0, 0, 0)) =
        abs(this.x - element.x) + abs(this.y - element.y) + abs(this.z - element.z)

    private fun getScanners() = Util.getInputAsListOfString("day19-input.txt")
        .map {
            if (it.contains(','))
                it.split(',').let { s -> Position(s[0].toInt(), s[1].toInt(), s[2].toInt()) }
            else null
        }
        .splitWhen { it == null }
        .mapIndexed { index, it -> Scanner(index, it.filterNotNull().toSet()) }

    @Test
    fun `face Tests`() {
        assertEquals(Position(1, 2, 3), Position(1, 2, 3).face(Turn.NONE, Rotate.NONE))
        assertEquals(Position(2, -1, 3), Position(1, 2, 3).face(Turn.NONE, Rotate.CLOCKWISE))
        assertEquals(Position(-2, 1, 3), Position(1, 2, 3).face(Turn.NONE, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(-1, -2, 3), Position(1, 2, 3).face(Turn.NONE, Rotate.UPSIDE_DOWN))

        assertEquals(Position(3, 2, -1), Position(1, 2, 3).face(Turn.RIGHT, Rotate.NONE))
        assertEquals(Position(3, -1, -2), Position(1, 2, 3).face(Turn.RIGHT, Rotate.CLOCKWISE))
        assertEquals(Position(3, 1, 2), Position(1, 2, 3).face(Turn.RIGHT, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(3, -2, 1), Position(1, 2, 3).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))

        assertEquals(Position(-3, 2, 1), Position(1, 2, 3).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(-3, -1, 2), Position(1, 2, 3).face(Turn.LEFT, Rotate.CLOCKWISE))
        assertEquals(Position(-3, 1, -2), Position(1, 2, 3).face(Turn.LEFT, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(-3, -2, -1), Position(1, 2, 3).face(Turn.LEFT, Rotate.UPSIDE_DOWN))

        assertEquals(Position(1, 3, -2), Position(1, 2, 3).face(Turn.UP, Rotate.NONE))
        assertEquals(Position(2, 3, 1), Position(1, 2, 3).face(Turn.UP, Rotate.CLOCKWISE))
        assertEquals(Position(-2, 3, -1), Position(1, 2, 3).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(-1, 3, 2), Position(1, 2, 3).face(Turn.UP, Rotate.UPSIDE_DOWN))

        assertEquals(Position(1, -3, 2), Position(1, 2, 3).face(Turn.DOWN, Rotate.NONE))
        assertEquals(Position(-2, -3, 1), Position(1, 2, 3).face(Turn.DOWN, Rotate.CLOCKWISE))
        assertEquals(Position(2, -3, -1), Position(1, 2, 3).face(Turn.DOWN, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(-1, -3, -2), Position(1, 2, 3).face(Turn.DOWN, Rotate.UPSIDE_DOWN))

        assertEquals(Position(-1, 2, -3), Position(1, 2, 3).face(Turn.BACKWARDS, Rotate.NONE))
        assertEquals(Position(2, 1, -3), Position(1, 2, 3).face(Turn.BACKWARDS, Rotate.CLOCKWISE))
        assertEquals(Position(-2, -1, -3), Position(1, 2, 3).face(Turn.BACKWARDS, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(1, -2, -3), Position(1, 2, 3).face(Turn.BACKWARDS, Rotate.UPSIDE_DOWN))


        assertEquals(Position(1,-1,1), Position(-1,-1,1).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Position(2,-2,2), Position(-2,-2,2).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Position(3,-3,3), Position(-3,-3,3).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Position(2,-1,3), Position(-2,-3,1).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Position(-5,4,-6), Position(5,6,-4).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Position(-8,-7,0), Position(8,0,7).face(Turn.DOWN, Rotate.UPSIDE_DOWN))

        assertEquals(Position(-1,-1,-1), Position(-1,-1,1).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(-2,-2,-2), Position(-2,-2,2).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(-3,-3,-3), Position(-3,-3,3).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(-1,-3,-2), Position(-2,-3,1).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(4,6,5), Position(5,6,-4).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Position(-7,0,8), Position(8,0,7).face(Turn.LEFT, Rotate.NONE))

        assertEquals(Position(1,1,-1), Position(-1,-1,1).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Position(2,2,-2), Position(-2,-2,2).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Position(3,3,-3), Position(-3,-3,3).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Position(1,3,-2), Position(-2,-3,1).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Position(-4,-6,5), Position(5,6,-4).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Position(7,0,8), Position(8,0,7).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))

        assertEquals(Position(1,1,1), Position(-1,-1,1).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(2,2,2), Position(-2,-2,2).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(3,3,3), Position(-3,-3,3).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(3,1,2), Position(-2,-3,1).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(-6,-4,-5), Position(5,6,-4).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Position(0,7,-8), Position(8,0,7).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
    }

    @Test
    fun `distance Tests`() {
        assertEquals(6, Position(0, 0, 0).distance(Position(1, 2, 3)))
        assertEquals(6, Position(1, 2, 3).distance())
        assertEquals(12, Position(-1, -2, -3).distance(Position(1, 2, 3)))
    }
}