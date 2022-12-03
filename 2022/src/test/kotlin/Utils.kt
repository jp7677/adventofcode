fun getPuzzleInputAsListOfInt(name: String, separator: String = System.lineSeparator()) =
    getPuzzleInput(name, separator).map(String::toInt)

fun getPuzzleInputAsListOfLong(name: String, separator: String = System.lineSeparator()) =
    getPuzzleInput(name, separator).map(String::toLong)

fun getPuzzleInput(name: String, separator: String = System.lineSeparator()) =
    getPuzzleInputAsString(name).split(separator)

fun getPuzzleInputAsString(name: String) =
    object {}::class.java.getResource(name)?.readText()
        ?: throw Exception("Invalid input name")
