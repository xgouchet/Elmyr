package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeStringSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region String by char

        context("forging strings as char arrays") {

            it("forges a String with given size") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.aString(size)

                    assertThat(string).hasSize(size)
                }
            }

            it("forges a String with selected characters") {
                repeat(testRepeatCountSmall) {
                    val string = forge.aString {
                        aChar('j', 'x')
                    }

                    assertThat(string).matches("[j-x]+")
                }
            }
        }

        // endregion

        // region String classes

        context("forging strings  from char classes") {
            it("forges an Ascii printable string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAsciiString()

                    string.forEach {
                        assertThat(it)
                                .isGreaterThanOrEqualTo(Forge.MIN_PRINTABLE)
                                .isLessThanOrEqualTo(Forge.MAX_ASCII)
                    }
                }
            }

            it("forges an Extended Ascii printable string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anExtendedAsciiString()

                    string.forEach {
                        assertThat(it)
                                .isGreaterThanOrEqualTo(Forge.MIN_PRINTABLE)
                                .isLessThanOrEqualTo(Forge.MAX_ASCII_EXTENDED)
                    }
                }
            }

            it("forges an alpha string with given size and default case") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.anAlphabeticalString(size = size)
                    assertThat(string)
                            .matches("[a-zA-Z]+")
                            .hasSize(size)
                }
            }

            it("forges an alpha string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphabeticalString(case = Case.ANY)
                    assertThat(string).matches("[a-zA-Z]+")
                }
            }

            it("forges an alpha uppercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphabeticalString(case = Case.UPPER)
                    assertThat(string).matches("[A-Z]+")
                }
            }

            it("forges an alpha lowercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphabeticalString(case = Case.LOWER)
                    assertThat(string).matches("[a-z]+")
                }
            }

            it("forges an alpha digit string with given size and default case") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.anAlphaNumericalString(size = size)
                    assertThat(string)
                            .matches("[a-zA-Z0-9]+")
                            .hasSize(size)
                }
            }

            it("forges an alpha digit string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphaNumericalString(case = Case.ANY)
                    assertThat(string).matches("[a-zA-Z0-9]+")
                }
            }

            it("forges an alpha digit uppercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphaNumericalString(case = Case.UPPER)
                    assertThat(string).matches("[A-Z0-9]+")
                }
            }

            it("forges an alpha digit lowercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anAlphaNumericalString(case = Case.LOWER)
                    assertThat(string).matches("[a-z0-9]+")
                }
            }

            it("forges a digit string with given size ") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.aNumericalString(size)
                    assertThat(string)
                            .matches("[0-9]+")
                            .hasSize(size)
                }
            }

            it("forges a digit string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.aNumericalString()
                    assertThat(string).matches("[0-9]+")
                }
            }

            it("forges an hexadecimal string with given size and default case") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.anHexadecimalString(size = size)
                    assertThat(string)
                            .matches("[a-fA-F0-9]+")
                            .hasSize(size)
                }
            }

            it("forges an hexadecimal string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anHexadecimalString(case = Case.ANY)
                    assertThat(string).matches("[a-fA-F0-9]+")
                }
            }

            it("forges an hexadecimal uppercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anHexadecimalString(case = Case.UPPER)
                    assertThat(string).matches("[A-F0-9]+")
                }
            }

            it("forges an hexadecimal lowercase string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.anHexadecimalString(case = Case.LOWER)
                    assertThat(string).matches("[a-f0-9]+")
                }
            }

            it("forges a whitespace string") {
                repeat(testRepeatCountSmall) {
                    val string = forge.aWhitespaceString()
                    assertThat(string).matches("\\s+")
                }
            }
            it("forges a whitespace string with given size") {
                repeat(testRepeatCountSmall) {
                    val size = forge.aTinyInt()
                    val string = forge.aWhitespaceString(size)
                    assertThat(string)
                            .matches("\\s+")
                            .hasSize(size)
                }
            }
        }

        // endregion

        // region String Regex
        context("forging regex based strings ") {

            // No need to be exhaustive, the regex package is heavily tested
            val regexes = arrayOf(
                    "foo",
                    "[a-fA-F0-9]+"
            )

            regexes.forEach {

                it("forges string matching /$it/") {
                    val regex = it
                    repeat(testRepeatCountSmall) {
                        val res = forge.aStringMatching(regex)
                        assertThat(res)
                                .matches(regex)

                        val res2 = forge.aStringMatching(Regex(regex))
                        assertThat(res2)
                                .matches(regex)
                    }
                }
            }
        }

        // endregion
    }
})
