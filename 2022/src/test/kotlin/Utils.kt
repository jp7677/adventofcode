fun getPuzzleInput(name: String, separator: String = eol()) =
    object {}::class.java.getResourceAsStream(name)
        ?.bufferedReader()
        ?.let {
            if (separator == eol())
                it.lineSequence()
            else
                it.readText().split(separator).asSequence()
        }
        ?: throw Exception("Invalid input name")

fun eol(): String = System.lineSeparator()
