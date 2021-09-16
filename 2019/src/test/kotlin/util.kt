class Util {
    companion object {
        fun getInputAsString(name: String) =
            Util::class.java.getResource(name)?.readText()
                ?: throw Exception("Invalid input name")

        fun getInputAsListOfString(name: String, separator: String = System.lineSeparator()) =
            getInputAsString(name).split(separator)

        fun getInputAsListOfInt(name: String, separator: String = System.lineSeparator()) =
            getInputAsListOfString(name, separator).map(String::toInt)
    }
}

fun <V> List<V>.permutations(): List<List<V>> {
    val listOfPermutations: MutableList<List<V>> = mutableListOf()

    fun swap(list: MutableList<V>, i: Int, j: Int) {
        list[i] = list.set(j, list[i])
    }

    fun generate(k: Int, list: MutableList<V>) {
        if (k == 1)
            listOfPermutations.add(list.toList())
        else
            for (i in 0 until k) {
                generate(k - 1, list)
                if (k % 2 == 0)
                    swap(list, i, k - 1)
                else
                    swap(list, 0, k - 1)
            }
    }

    generate(this.count(), this.toMutableList())
    return listOfPermutations
}