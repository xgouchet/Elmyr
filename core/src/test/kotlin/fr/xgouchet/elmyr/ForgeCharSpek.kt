package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeCharSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region Char in Range

        context("forging chars ") {

            it("fails if min > max") {
                val min = forge.aChar(min = '\u0100')
                val max = forge.aChar(max = '\u0100')
                throws<IllegalArgumentException> {
                    forge.aChar(min, max)
                }
            }

            it("fails if min == max") {
                val min = forge.aChar()
                throws<IllegalArgumentException> {
                    forge.aChar(min, min)
                }
            }

            it("forges a char in a specified range") {
                val min = forge.aChar()
                val max = forge.aChar(min = min + 1)

                repeat(testRepeatCountSmall) {
                    val char = forge.aChar(min, max)
                    assertThat(char)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a char in minimal range") {
                val min = forge.aChar()
                val max = min + 1

                repeat(testRepeatCountSmall) {
                    val char = forge.aChar(min, max)
                    assertThat(char)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a char above a min") {
                val min = forge.aChar(min = '\u0100')

                repeat(testRepeatCountSmall) {
                    val char = forge.aChar(min = min)
                    assertThat(char)
                            .isGreaterThanOrEqualTo(min)
                }
            }

            it("forges a char below a max") {
                val max = forge.aChar(min = '\u0100')

                repeat(testRepeatCountSmall) {
                    val char = forge.aChar(max = max)
                    assertThat(char)
                            .isLessThan(max)
                }
            }
        }

        // endregion

        // region Char classes

        context("forging characters from char classes") {
            it("forges an Ascii printable character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAsciiChar()
                    assertThat(char)
                            .isGreaterThanOrEqualTo(Forge.MIN_PRINTABLE)
                            .isLessThanOrEqualTo(Forge.MAX_ASCII)
                }
            }

            it("forges an Extended Ascii printable character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anExtendedAsciiChar()
                    assertThat(char)
                            .isGreaterThanOrEqualTo(Forge.MIN_PRINTABLE)
                            .isLessThanOrEqualTo(Forge.MAX_ASCII_EXTENDED)
                }
            }

            it("forges an alpha character with default case") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphabeticalChar()
                    assertThat(char.toString()).matches("[a-zA-Z]")
                }
            }

            it("forges an alpha character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphabeticalChar(case = Case.ANY)
                    assertThat(char.toString()).matches("[a-zA-Z]")
                }
            }

            it("forges an alpha uppercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphabeticalChar(case = Case.UPPER)
                    assertThat(char.toString()).matches("[A-Z]")
                }
            }

            it("forges an alpha lowercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphabeticalChar(case = Case.LOWER)
                    assertThat(char.toString()).matches("[a-z]")
                }
            }

            it("forges an alpha digit character with default case") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphaNumericalChar()
                    assertThat(char.toString()).matches("[a-zA-Z0-9]")
                }
            }

            it("forges an alpha digit character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphaNumericalChar(case = Case.ANY)
                    assertThat(char.toString()).matches("[a-zA-Z0-9]")
                }
            }

            it("forges an alpha digit uppercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphaNumericalChar(case = Case.UPPER)
                    assertThat(char.toString()).matches("[A-Z0-9]")
                }
            }

            it("forges an alpha digit lowercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anAlphaNumericalChar(case = Case.LOWER)
                    assertThat(char.toString()).matches("[a-z0-9]")
                }
            }

            it("forges a digit character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.aNumericalChar()
                    assertThat(char.toString()).matches("[0-9]")
                }
            }

            it("forges an hexadecimal character with default case") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anHexadecimalChar()
                    assertThat(char.toString()).matches("[a-fA-F0-9]")
                }
            }

            it("forges an hexadecimal character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anHexadecimalChar(case = Case.ANY)
                    assertThat(char.toString()).matches("[a-fA-F0-9]")
                }
            }

            it("forges an hexadecimal uppercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anHexadecimalChar(case = Case.UPPER)
                    assertThat(char.toString()).matches("[A-F0-9]")
                }
            }

            it("forges an hexadecimal lowercase character") {
                repeat(testRepeatCountSmall) {
                    val char = forge.anHexadecimalChar(case = Case.LOWER)
                    assertThat(char.toString()).matches("[a-f0-9]")
                }
            }

            it("forges a whitespace") {
                repeat(testRepeatCountSmall) {
                    val char = forge.aWhitespaceChar()
                    assertThat(char.toString()).matches("\\s")
                }
            }
        }

        // endregion
    }
})
