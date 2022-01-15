package fr.xgouchet.elmyr.semantics.markov

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
class MarkovReaderTest {

    @Test
    fun parseNames(forge: Forge) {
        val reader = MarkovReader()

        val table = reader.parse("/first_name_mkv.csv")

        assertThat(table.tokens)
            .isEqualTo("abcdefghijklmnopqrstuvwxyz".toCharArray())
        assertThat(table.chainLength)
            .isEqualTo(4)

        println("Gen first name: ${table.generate(forge).capitalize()}")
    }

    @Test
    fun parseLastNames(forge: Forge) {
        val reader = MarkovReader()

        val table = reader.parse("/last_name_mkv.csv")

        assertThat(table.tokens)
            .isEqualTo("abcdefghijklmnopqrstuvwxyz".toCharArray())
        assertThat(table.chainLength)
            .isEqualTo(4)

        println("Gen last name: ${table.generate(forge).capitalize()}")
    }

    @Test
    fun parseLipsum(forge: Forge) {
        val reader = MarkovReader()

        val table = reader.parse("/lipsum_mkv.csv")

        assertThat(table.tokens)
            .isEqualTo("abcdefghijklmnopqrstuvwxyz.; ".toCharArray())
        assertThat(table.chainLength)
            .isEqualTo(4)

        println("Gen lipsum: ${table.generate(forge).capitalize()}")
    }
}
