import kotlin.test.Test
import kotlin.test.assertEquals

class Day23 {

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day23-input.txt")

        /*
        #############
        #...........#
        ###D#A#C#A###
          #D#C#B#B#
          #########
        energy = 0

        #############
        #.A.......A.#
        ###D#.#C#.###
          #D#C#B#B#
          #########
        energy = 6

        #############
        #.A.B...C.A.#
        ###D#.#.#.###
          #D#C#.#B#
          #########
        energy = 256

        #############
        #.A.B.....A.#
        ###D#.#C#.###
          #D#.#C#B#
          #########
        energy = 1056

        #############
        #.A.......A.#
        ###D#B#C#.###
          #D#B#C#.#
          #########
        energy = 1156

        #############
        #.A.......A.#
        ###.#B#C#D###
          #.#B#C#D#
          #########
        energy = 19156

        #############
        #...........#
        ###A#B#C#D###
          #A#B#C#D#
          #########
        energy = 19167
         */

        val energy = 19167

        assertEquals(19167, energy)
    }
}
