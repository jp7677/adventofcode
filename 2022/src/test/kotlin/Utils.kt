fun getPuzzleInput(name: String, separator: String = System.lineSeparator()) =
    object {}::class.java.getResourceAsStream(name)
        ?.bufferedReader()
        ?.let {
            if (separator == System.lineSeparator())
                it.lineSequence()
            else
                it.readText().split(separator).asSequence()
        }
        ?: throw Exception("Invalid input name")
