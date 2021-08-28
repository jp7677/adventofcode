class Util {
    companion object {
        fun getInputAsListOfString(name: String, separator: String = System.lineSeparator()): List<String> =
            getInputAsString(name).split(separator)

        fun getInputAsListOfInt(name: String, separator: String = System.lineSeparator()): List<Int> =
            getInputAsListOfString(name, separator).map(String::toInt)

        private fun getInputAsString(name: String): String =
            Util::class.java.getResource(name)?.readText()
                ?: throw Exception("Invalid input name")

    }
}