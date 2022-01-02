import Util.splitWhen
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19 {
    data class Beacon(val x: Int, val y: Int, val z: Int)
    data class Scanner(val index: Int, val beacons: List<Beacon>)
    enum class Turn {
        /** negative Z */ NONE,
        /** positive X */ RIGHT,
        /** negative X */ LEFT,
        /** positive Y */ UP,
        /** negative Y */ DOWN,
        /** positive Z */ BACKWARDS
    }
    enum class Rotate { NONE, CLOCKWISE, COUNTERCLOCKWISE, UPSIDE_DOWN }
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

    private fun getOceanMap(scanners: List<Scanner>): Pair<MutableList<Pair<Int, Beacon>>, MutableSet<Beacon>> {
        val detectedScanners = mutableListOf(scanners.first().index to Beacon(0, 0, 0))
        val detectedBeacons = scanners.first().beacons.toMutableSet()

        while (detectedScanners.size != scanners.size) {
            scanners
                .filterNot { detectedScanners.map { scanner -> scanner.first }.contains(it.index) }
                .map {
                    it to it.beacons.getTrianglesWithBeacons().filter { (triangles, _) ->
                        triangles.sorted() in detectedBeacons.toList().getTrianglesWithBeacons()
                            .map { (triangle, _) -> triangle.sorted() }
                    }
                }
                .filter { (_, trianglesWithBeacons) -> trianglesWithBeacons.count() * 3 >= 12 }
                .forEach { (scanner, trianglesWithBeacons) ->
                    val perspectiveCorrectedScanner = trianglesWithBeacons
                        .map { (triangle, beacons) ->
                            val (_, matchingDetectedBeacons) = detectedBeacons.toList().getTrianglesWithBeacons()
                                .single { (detectedTriangle, _) -> triangle == detectedTriangle }

                            turns.flatMap { turn ->
                                rotations.mapNotNull { rotation ->
                                    val faced = beacons[0].face(turn, rotation)
                                    val distance1 = matchingDetectedBeacons[0].distance(faced)
                                    val distance2 = matchingDetectedBeacons[1].distance(beacons[1].face(turn, rotation))
                                    val distance3 = matchingDetectedBeacons[2].distance(beacons[2].face(turn, rotation))
                                    if (distance1 == distance2 && distance1 == distance3) {
                                        Triple(
                                            turn,
                                            rotation,
                                            Triple(
                                                faced.x - matchingDetectedBeacons[0].x,
                                                faced.y - matchingDetectedBeacons[0].y,
                                                faced.z - matchingDetectedBeacons[0].z
                                            )
                                        )
                                    } else null
                                }
                            }.toSet()
                        }
                        .let {
                            val direction = it
                                .filterNot { directions -> directions.isEmpty() }
                                .reduce { acc, directions -> acc intersect directions }
                                .let { directions ->
                                    if (directions.size == 1) directions.first() else throw IllegalStateException()
                                }

                            detectedScanners.add(
                                scanner.index to Beacon(
                                    direction.third.first,
                                    direction.third.second,
                                    direction.third.third
                                )
                            )

                            Scanner(
                                scanner.index,
                                scanner.beacons.map { beacon ->
                                    val faced = beacon.face(direction.first, direction.second)
                                    Beacon(
                                        faced.x - direction.third.first,
                                        faced.y - direction.third.second,
                                        faced.z - direction.third.third
                                    )
                                }
                            )
                        }

                    perspectiveCorrectedScanner.beacons
                        .forEach {
                            detectedBeacons.add(it)
                        }
                }
        }
        return Pair(detectedScanners, detectedBeacons)
    }

    private fun List<Beacon>.getTrianglesWithBeacons() =
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
            .sortedWith(compareBy<Pair<List<Int>, List<Beacon>>> { (triangle, _) -> triangle[0] }
                .thenBy { (triangle, _) -> triangle[1] } )
            .distinctBy { (triangle, _) -> triangle.sorted() }

    private fun Beacon.face(turn: Turn, rotation: Rotate) = when (turn to rotation) {
        Turn.NONE to Rotate.NONE                  -> this
        Turn.NONE to Rotate.CLOCKWISE             -> Beacon(this.y,      this.x * -1, this.z)
        Turn.NONE to Rotate.COUNTERCLOCKWISE      -> Beacon(this.y * -1, this.x,      this.z)
        Turn.NONE to Rotate.UPSIDE_DOWN           -> Beacon(this.x * -1, this.y * -1, this.z)
        Turn.RIGHT to Rotate.NONE                 -> Beacon(this.z,      this.y,      this.x * -1)
        Turn.RIGHT to Rotate.CLOCKWISE            -> Beacon(this.z,      this.x * -1, this.y * -1)
        Turn.RIGHT to Rotate.COUNTERCLOCKWISE     -> Beacon(this.z,      this.x,      this.y)
        Turn.RIGHT to Rotate.UPSIDE_DOWN          -> Beacon(this.z,      this.y * -1, this.x)
        Turn.LEFT to Rotate.NONE                  -> Beacon(this.z * -1, this.y,      this.x)
        Turn.LEFT to Rotate.CLOCKWISE             -> Beacon(this.z * -1, this.x * -1, this.y)
        Turn.LEFT to Rotate.COUNTERCLOCKWISE      -> Beacon(this.z * -1, this.x,      this.y * -1)
        Turn.LEFT to Rotate.UPSIDE_DOWN           -> Beacon(this.z * -1, this.y * -1, this.x * -1)
        Turn.UP to Rotate.NONE                    -> Beacon(this.x,      this.z,      this.y * -1)
        Turn.UP to Rotate.CLOCKWISE               -> Beacon(this.y,      this.z,      this.x)
        Turn.UP to Rotate.COUNTERCLOCKWISE        -> Beacon(this.y * -1, this.z,      this.x * -1)
        Turn.UP to Rotate.UPSIDE_DOWN             -> Beacon(this.x * -1, this.z,      this.y)
        Turn.DOWN to Rotate.NONE                  -> Beacon(this.x,      this.z * -1, this.y)
        Turn.DOWN to Rotate.CLOCKWISE             -> Beacon(this.y * -1, this.z * -1, this.x)
        Turn.DOWN to Rotate.COUNTERCLOCKWISE      -> Beacon(this.y,      this.z * -1, this.x * -1)
        Turn.DOWN to Rotate.UPSIDE_DOWN           -> Beacon(this.x * -1, this.z * -1, this.y * -1)
        Turn.BACKWARDS to Rotate.NONE             -> Beacon(this.x * -1, this.y,      this.z * -1)
        Turn.BACKWARDS to Rotate.CLOCKWISE        -> Beacon(this.y,      this.x,      this.z * -1)
        Turn.BACKWARDS to Rotate.COUNTERCLOCKWISE -> Beacon(this.y * -1, this.x * -1, this.z * -1)
        Turn.BACKWARDS to Rotate.UPSIDE_DOWN      -> Beacon(this.x,      this.y * -1, this.z * -1)
        else -> throw IllegalStateException()
    }

    private fun Beacon.distance(element: Beacon = Beacon(0, 0, 0)) =
        abs(this.x - element.x) + abs(this.y - element.y) + abs(this.z - element.z)

    private fun getScanners() = Util.getInputAsListOfString("day19-input.txt")
        .map {
            if (it.contains(','))
                it.split(',').let { s -> Beacon(s[0].toInt(), s[1].toInt(), s[2].toInt()) }
            else null
        }
        .splitWhen { it == null }
        .mapIndexed { index, it -> Scanner(index, it.filterNotNull()) }

    @Test
    fun `face Tests`() {
        assertEquals(Beacon(1, 2, 3), Beacon(1, 2, 3).face(Turn.NONE, Rotate.NONE))
        assertEquals(Beacon(2, -1, 3), Beacon(1, 2, 3).face(Turn.NONE, Rotate.CLOCKWISE))
        assertEquals(Beacon(-2, 1, 3), Beacon(1, 2, 3).face(Turn.NONE, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(-1, -2, 3), Beacon(1, 2, 3).face(Turn.NONE, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(3, 2, -1), Beacon(1, 2, 3).face(Turn.RIGHT, Rotate.NONE))
        assertEquals(Beacon(3, -1, -2), Beacon(1, 2, 3).face(Turn.RIGHT, Rotate.CLOCKWISE))
        assertEquals(Beacon(3, 1, 2), Beacon(1, 2, 3).face(Turn.RIGHT, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(3, -2, 1), Beacon(1, 2, 3).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(-3, 2, 1), Beacon(1, 2, 3).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(-3, -1, 2), Beacon(1, 2, 3).face(Turn.LEFT, Rotate.CLOCKWISE))
        assertEquals(Beacon(-3, 1, -2), Beacon(1, 2, 3).face(Turn.LEFT, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(-3, -2, -1), Beacon(1, 2, 3).face(Turn.LEFT, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(1, 3, -2), Beacon(1, 2, 3).face(Turn.UP, Rotate.NONE))
        assertEquals(Beacon(2, 3, 1), Beacon(1, 2, 3).face(Turn.UP, Rotate.CLOCKWISE))
        assertEquals(Beacon(-2, 3, -1), Beacon(1, 2, 3).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(-1, 3, 2), Beacon(1, 2, 3).face(Turn.UP, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(1, -3, 2), Beacon(1, 2, 3).face(Turn.DOWN, Rotate.NONE))
        assertEquals(Beacon(-2, -3, 1), Beacon(1, 2, 3).face(Turn.DOWN, Rotate.CLOCKWISE))
        assertEquals(Beacon(2, -3, -1), Beacon(1, 2, 3).face(Turn.DOWN, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(-1, -3, -2), Beacon(1, 2, 3).face(Turn.DOWN, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(-1, 2, -3), Beacon(1, 2, 3).face(Turn.BACKWARDS, Rotate.NONE))
        assertEquals(Beacon(2, 1, -3), Beacon(1, 2, 3).face(Turn.BACKWARDS, Rotate.CLOCKWISE))
        assertEquals(Beacon(-2, -1, -3), Beacon(1, 2, 3).face(Turn.BACKWARDS, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(1, -2, -3), Beacon(1, 2, 3).face(Turn.BACKWARDS, Rotate.UPSIDE_DOWN))


        assertEquals(Beacon(1,-1,1), Beacon(-1,-1,1).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(2,-2,2), Beacon(-2,-2,2).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(3,-3,3), Beacon(-3,-3,3).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(2,-1,3), Beacon(-2,-3,1).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(-5,4,-6), Beacon(5,6,-4).face(Turn.DOWN, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(-8,-7,0), Beacon(8,0,7).face(Turn.DOWN, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(-1,-1,-1), Beacon(-1,-1,1).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(-2,-2,-2), Beacon(-2,-2,2).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(-3,-3,-3), Beacon(-3,-3,3).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(-1,-3,-2), Beacon(-2,-3,1).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(4,6,5), Beacon(5,6,-4).face(Turn.LEFT, Rotate.NONE))
        assertEquals(Beacon(-7,0,8), Beacon(8,0,7).face(Turn.LEFT, Rotate.NONE))

        assertEquals(Beacon(1,1,-1), Beacon(-1,-1,1).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(2,2,-2), Beacon(-2,-2,2).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(3,3,-3), Beacon(-3,-3,3).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(1,3,-2), Beacon(-2,-3,1).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(-4,-6,5), Beacon(5,6,-4).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))
        assertEquals(Beacon(7,0,8), Beacon(8,0,7).face(Turn.RIGHT, Rotate.UPSIDE_DOWN))

        assertEquals(Beacon(1,1,1), Beacon(-1,-1,1).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(2,2,2), Beacon(-2,-2,2).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(3,3,3), Beacon(-3,-3,3).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(3,1,2), Beacon(-2,-3,1).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(-6,-4,-5), Beacon(5,6,-4).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
        assertEquals(Beacon(0,7,-8), Beacon(8,0,7).face(Turn.UP, Rotate.COUNTERCLOCKWISE))
    }

    @Test
    fun `distance Tests`() {
        assertEquals(6, Beacon(0, 0, 0).distance(Beacon(1, 2, 3)))
        assertEquals(6, Beacon(1, 2, 3).distance())
        assertEquals(12, Beacon(-1, -2, -3).distance(Beacon(1, 2, 3)))
    }
}