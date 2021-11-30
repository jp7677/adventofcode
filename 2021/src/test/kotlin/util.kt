class Util {
    companion object {
        fun getInputAsString(name: String) =
            Util::class.java.getResource(name)?.readText()
                ?: throw Exception("Invalid input name")

        fun getInputAsListOfString(name: String, separator: String = System.lineSeparator()) =
            getInputAsString(name).split(separator)

        fun getInputAsListOfInt(name: String, separator: String = System.lineSeparator()) =
            getInputAsListOfString(name, separator).map(String::toInt)

        fun getInputAsListOfLong(name: String, separator: String = System.lineSeparator()) =
            getInputAsListOfString(name, separator).map(String::toLong)
    }
}
