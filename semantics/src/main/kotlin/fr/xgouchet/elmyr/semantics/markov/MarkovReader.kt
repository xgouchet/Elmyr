package fr.xgouchet.elmyr.semantics.markov

import java.io.InputStream

internal class MarkovReader {

    fun parse(resourceName: String): MarkovTable {
        val stream = javaClass.getResourceAsStream(resourceName)
        checkNotNull(stream) { "Cannot load resource $resourceName: stream is null" }
        return parse(stream)
    }

    @Suppress("NestedBlockDepth")
    private fun parse(stream: InputStream): MarkovTable {
        var table: MarkovTable? = null

        stream.bufferedReader().use { reader ->
            val headerLine = reader.readLine()
            val tokens = headerLine.split(',')
            check(tokens.last() == "${MarkovTable.NULL_CHAR}") {
                "Header line doesn't match expectation ${MarkovTable.NULL_CHAR}"
            }

            val chainLength = tokens.first().toInt()
            val actualTokens = tokens.subList(1, tokens.size - 1).map {
                check(it.length == 1)
                it.first()
            }.toCharArray()

            val ongoingTable = MarkovTable(actualTokens, chainLength)
            var line: String?

            do {
                line = reader.readLine()
                val values = line?.split(',')
                if (values != null) {
                    val prefix = values.first()
                    for (i in 1 until tokens.size) {
                        val word = "$prefix${tokens[i]}"
                        val weight = values[i].toInt()
                        ongoingTable.sample(weight, word)
                    }
                }
            } while (line != null)

            table = ongoingTable
        }

        val output = table
        checkNotNull(output) { "Unable to initialize Markov table" }

        return output
    }
}
