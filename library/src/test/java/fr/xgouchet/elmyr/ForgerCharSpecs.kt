package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.Forger.Companion.ALPHA
import fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM
import fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM_LOWER
import fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM_UPPER
import fr.xgouchet.elmyr.Forger.Companion.ALPHA_LOWER
import fr.xgouchet.elmyr.Forger.Companion.ALPHA_UPPER
import fr.xgouchet.elmyr.Forger.Companion.CONSONANT
import fr.xgouchet.elmyr.Forger.Companion.CONSONANT_LOWER
import fr.xgouchet.elmyr.Forger.Companion.CONSONANT_UPPER
import fr.xgouchet.elmyr.Forger.Companion.DIGIT
import fr.xgouchet.elmyr.Forger.Companion.HEXA_LOWER
import fr.xgouchet.elmyr.Forger.Companion.HEXA_UPPER
import fr.xgouchet.elmyr.Forger.Companion.PRINTABLE_ASCII
import fr.xgouchet.elmyr.Forger.Companion.VOWEL
import fr.xgouchet.elmyr.Forger.Companion.VOWEL_LOWER
import fr.xgouchet.elmyr.Forger.Companion.VOWEL_UPPER
import fr.xgouchet.elmyr.Forger.Companion.WHITESPACE
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ForgerCharSpecs : io.kotlintest.specs.FeatureSpec() {

    init {

        feature("A Char Forger uses a seed") {

            val forger = fr.xgouchet.elmyr.Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = CharArray(16) { forger.aChar() }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = fr.xgouchet.elmyr.Forger()
                otherForger.reset(seed)
                val otherData = CharArray(16) { otherForger.aChar() }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = CharArray(16) { forger.aChar() }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = CharArray(16) { forger.aChar() }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("A Char Forger provides chars of all sorts") {
            val forger = fr.xgouchet.elmyr.Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Produce an Ascii printable character") {
                repeat(16, {
                    val char = forger.anAsciiChar()
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.PRINTABLE_ASCII)
                            .contains(char)
                })
            }

            scenario("Produce an alpha character") {
                repeat(16, {
                    val char = forger.anAlphaChar(case = fr.xgouchet.elmyr.Case.ANY)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA)
                            .contains(char)
                })
            }

            scenario("Produce an alpha uppercase character") {
                repeat(16, {
                    val char = forger.anAlphaChar(case = fr.xgouchet.elmyr.Case.UPPER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce an alpha lowercase character") {
                repeat(16, {
                    val char = forger.anAlphaChar(case = fr.xgouchet.elmyr.Case.LOWER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce an alpha digit character") {
                repeat(16, {
                    val char = forger.anAlphaNumChar(case = fr.xgouchet.elmyr.Case.ANY)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM)
                            .contains(char)
                })
            }

            scenario("Produce an alpha digit uppercase character") {
                repeat(16, {
                    val char = forger.anAlphaNumChar(case = fr.xgouchet.elmyr.Case.UPPER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce a non alpha digit uppercase character") {
                repeat(16, {
                    val char = forger.aNonAlphaNumChar()
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM_UPPER)
                            .doesNotContain(char)
                })
            }

            scenario("Produce an alpha digit lowercase character") {
                repeat(16, {
                    val char = forger.anAlphaNumChar(case = fr.xgouchet.elmyr.Case.LOWER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.ALPHA_NUM_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce an digit character") {
                repeat(16, {
                    val char = forger.aDigitChar()
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.DIGIT)
                            .contains(char)
                })
            }

            scenario("Produce a non digit character") {
                repeat(16, {
                    val char = forger.aNonDigitChar()
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.DIGIT)
                            .doesNotContain(char)
                })
            }

            scenario("Produce an hexa-digit character") {
                repeat(16, {
                    val char = forger.anHexadecimalChar(case = fr.xgouchet.elmyr.Case.LOWER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.HEXA_LOWER)
                            .contains(char)
                })
            }
            scenario("Produce an hexa-digit character") {
                repeat(16, {
                    val char = forger.anHexadecimalChar(case = fr.xgouchet.elmyr.Case.UPPER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.HEXA_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce a vowel character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = fr.xgouchet.elmyr.Case.ANY)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.VOWEL)
                            .contains(char)
                })
            }
            scenario("Produce a vowel uppercase character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = fr.xgouchet.elmyr.Case.UPPER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.VOWEL_UPPER)
                            .contains(char)
                })
            }
            scenario("Produce a vowel lowercase character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = fr.xgouchet.elmyr.Case.LOWER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.VOWEL_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce a consonant character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = fr.xgouchet.elmyr.Case.ANY)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.CONSONANT)
                            .contains(char)
                })
            }
            scenario("Produce a consonant uppercase character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = fr.xgouchet.elmyr.Case.UPPER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.CONSONANT_UPPER)
                            .contains(char)
                })
            }
            scenario("Produce a consonant lowercase character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = fr.xgouchet.elmyr.Case.LOWER)
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.CONSONANT_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce a whitespace") {
                repeat(16, {
                    val char = forger.aWhitespaceChar()
                    assertThat(fr.xgouchet.elmyr.Forger.Companion.WHITESPACE)
                            .contains(char)
                })
            }
        }
    }
}