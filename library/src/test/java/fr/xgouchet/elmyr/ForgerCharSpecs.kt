package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ForgerCharSpecs : FeatureSpec() {

    init {

        feature("A Char Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = CharArray(16) { forger.aChar(CharConstraint.ANY) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = CharArray(16) { otherForger.aChar(CharConstraint.ANY) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = CharArray(16) { forger.aChar(CharConstraint.ANY) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = CharArray(16) { forger.aChar(CharConstraint.ANY) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("A Char Forger provides chars of all sorts") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Produce an Ascii printable character") {
                repeat(16, {
                    val char = forger.anAsciiChar()
                    assertThat(Forger.Companion.PRINTABLE_ASCII)
                            .contains(char)
                })
            }

            scenario("Produce an alpha character") {
                repeat(16, {
                    val char = forger.anAlphabeticalChar(case = Case.ANY)
                    assertThat(Forger.Companion.ALPHA)
                            .contains(char)
                })
            }

            scenario("Produce an alpha uppercase character") {
                repeat(16, {
                    val char = forger.anAlphabeticalChar(case = Case.UPPER)
                    assertThat(Forger.Companion.ALPHA_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce an alpha lowercase character") {
                repeat(16, {
                    val char = forger.anAlphabeticalChar(case = Case.LOWER)
                    assertThat(Forger.Companion.ALPHA_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce an alpha digit character") {
                repeat(16, {
                    val char = forger.anAlphaNumericalChar(case = Case.ANY)
                    assertThat(Forger.Companion.ALPHA_NUM)
                            .contains(char)
                })
            }

            scenario("Produce an alpha digit uppercase character") {
                repeat(16, {
                    val char = forger.anAlphaNumericalChar(case = Case.UPPER)
                    assertThat(Forger.Companion.ALPHA_NUM_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce a non alpha digit uppercase character") {
                repeat(16, {
                    val char = forger.aNonAlphaNumericalChar()
                    assertThat(Forger.Companion.ALPHA_NUM_UPPER)
                            .doesNotContain(char)
                })
            }

            scenario("Produce an alpha digit lowercase character") {
                repeat(16, {
                    val char = forger.anAlphaNumericalChar(case = Case.LOWER)
                    assertThat(Forger.Companion.ALPHA_NUM_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce an digit character") {
                repeat(16, {
                    val char = forger.aNumericalChar()
                    assertThat(Forger.Companion.DIGIT)
                            .contains(char)
                })
            }

            scenario("Produce a non digit character") {
                repeat(16, {
                    val char = forger.aNonNumericalChar()
                    assertThat(Forger.Companion.DIGIT)
                            .doesNotContain(char)
                })
            }

            scenario("Produce an hexa-digit character") {
                repeat(16, {
                    val char = forger.anHexadecimalChar(case = Case.LOWER)
                    assertThat(Forger.Companion.HEXA_LOWER)
                            .contains(char)
                })
            }
            scenario("Produce an hexa-digit character") {
                repeat(16, {
                    val char = forger.anHexadecimalChar(case = Case.UPPER)
                    assertThat(Forger.Companion.HEXA_UPPER)
                            .contains(char)
                })
            }

            scenario("Produce a vowel character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = Case.ANY)
                    assertThat(Forger.Companion.VOWEL)
                            .contains(char)
                })
            }
            scenario("Produce a vowel uppercase character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = Case.UPPER)
                    assertThat(Forger.Companion.VOWEL_UPPER)
                            .contains(char)
                })
            }
            scenario("Produce a vowel lowercase character") {
                repeat(16, {
                    val char = forger.aVowelChar(case = Case.LOWER)
                    assertThat(Forger.Companion.VOWEL_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce a consonant character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = Case.ANY)
                    assertThat(Forger.Companion.CONSONANT)
                            .contains(char)
                })
            }
            scenario("Produce a consonant uppercase character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = Case.UPPER)
                    assertThat(Forger.Companion.CONSONANT_UPPER)
                            .contains(char)
                })
            }
            scenario("Produce a consonant lowercase character") {
                repeat(16, {
                    val char = forger.aConsonantChar(case = Case.LOWER)
                    assertThat(Forger.Companion.CONSONANT_LOWER)
                            .contains(char)
                })
            }

            scenario("Produce a whitespace") {
                repeat(16, {
                    val char = forger.aWhitespaceChar()
                    assertThat(Forger.Companion.WHITESPACE)
                            .contains(char)
                })
            }
        }
    }
}