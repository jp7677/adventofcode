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

    @Test
    fun `run part 01`() {
        val input = Util.getInputAsListOfString("day23-input.txt")

        /*
        #############
        #...........#
        ###D#A#C#A###
          #D#C#B#A#
          #D#B#A#C#
          #D#C#B#B#
          #########
  
        #############
        #.......B.CA#
        ###D#.#.#A###
          #D#C#.#A#
          #D#B#A#C#
          #D#C#B#B#
          #########
 
        #############
        #AB.....B.CA#
        ###D#.#.#A###
          #D#C#.#A#
          #D#B#.#C#
          #D#C#.#B#
          #########
 
        #############
        #AB.B...B.CA#
        ###D#.#.#A###
          #D#.#.#A#
          #D#.#.#C#
          #D#C#C#B#
          #########
 
        #############
        #AB.B...B.CA#
        ###D#.#.#A###
          #D#.#.#A#
          #D#.#C#C#
          #D#.#C#B#
          #########
 
        #############
        #A.........A#
        ###D#.#.#A###
          #D#B#C#A#
          #D#B#C#C#
          #D#B#C#B#
          #########
 
        #############
        #AA.......AA#
        ###D#.#.#.###
          #D#B#C#.#
          #D#B#C#C#
          #D#B#C#B#
          #########
 
        #############
        #AA.......AA#
        ###D#B#C#.###
          #D#B#C#.#
          #D#B#C#.#
          #D#B#C#.#
          #########

        #############
        #AA.......AA#
        ###.#B#C#D###
          #.#B#C#D#
          #.#B#C#D#
          #.#B#C#D#
          #########

        #############
        #...........#
        ###A#B#C#D###
          #A#B#C#D#
          #A#B#C#D#
          #A#B#C#D#
          #########
        */

        val energy = 47665

        assertEquals(47665, energy)  
    }
}
