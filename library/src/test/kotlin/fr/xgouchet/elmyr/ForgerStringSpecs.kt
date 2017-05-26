package fr.xgouchet.elmyr

import io.kotlintest.matchers.match
import io.kotlintest.matchers.should
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ForgerStringSpecs : FeatureSpec() {

    init {

        feature("A String Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = Array(16) { forger.aString(size = it) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = Array(16) { otherForger.aString(size = it) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = Array(16) { forger.aString(size = it) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = Array(16) { forger.aString(size = it) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("A String Forger provide meaningful Strings") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Produce a lowercase word-like string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.aWord(Case.LOWER, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[aeiouy]?([zrtpqsdfghjklmwxcvbn][aeiouy])*[zrtpqsdfghjklmwxcvbn]?")
                })
            }

            scenario("Produce an uppercase word-like string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.aWord(Case.UPPER, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[AEIOUY]?([ZRTPQSDFGHJKLMWXCVBN][AEIOUY])*[ZRTPQSDFGHJKLMWXCVBN]?")
                })
            }

            scenario("Produce an anycase word-like string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.aWord(Case.ANY, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[aeiouyAEIOUY]?([zrtpqsdfghjklmwxcvbnZRTPQSDFGHJKLMWXCVBN][aeiouyAEIOUY])*[zrtpqsdfghjklmwxcvbnZRTPQSDFGHJKLMWXCVBN]?")
                })
            }

            scenario("Produce a capitalized word-like string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.aWord(Case.CAPITALIZE, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[A-Z][aeiouy]?([zrtpqsdfghjklmwxcvbn][aeiouy])*[zrtpqsdfghjklmwxcvbn]?")
                })
            }

            scenario("Produce a lowercase lipsum sentence") {
                repeat(16, {
                    val size = forger.aSmallInt()
                    val word = forger.aSentence(Case.LOWER, size)

                    assertThat(word)
                            .hasSize(size)
                            .matches("(([a-z]+ )*[a-z]+\\.)|‽")
                })
            }

            scenario("Produce an uppercase lipsum sentence") {
                repeat(16, {
                    val size = forger.aSmallInt()
                    val word = forger.aSentence(Case.UPPER, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("(([A-Z]+ )*[A-Z]+\\.)|‽")
                })
            }

            scenario("Produce a capitalized lipsum sentence") {
                repeat(16, {
                    val size = forger.aSmallInt()
                    val word = forger.aSentence(Case.CAPITALIZE, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("([A-Z][a-z]*( [A-Z][a-z]+)*\\.)|‽")
                })
            }

            scenario("Produce a sentence capitalized lipsum sentence") {
                repeat(16, {
                    val size = forger.aSmallInt()
                    val word = forger.aSentence(Case.CAPITALIZED_SENTENCE, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("([A-Z][a-z]*( [a-z]+)*\\.)|‽")
                })
            }

            scenario("Produce a sentence capitalized lipsum sentence of any size") {
                repeat(16, {
                    val word = forger.aSentence(Case.CAPITALIZED_SENTENCE)
                    assertThat(word)
                            .matches("[A-Z][a-z]+( [a-z]+)*\\.")
                })
            }

            scenario("Produce an hexadecimal string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.anHexadecimalString(Case.UPPER, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[A-F0-9]+")
                })
            }

            scenario("Produce an hexadecimal string") {
                repeat(16, {
                    val size = forger.aTinyInt()
                    val word = forger.anHexadecimalString(Case.LOWER, size)
                    assertThat(word)
                            .hasSize(size)
                            .matches("[a-f0-9]+")
                })
            }

            scenario("Produce a url string") {
                repeat(16, {
                    val url = forger.aUrl()
                    assertThat(url)
                            .matches(Regex("""[a-z]+://[a-z]+\.[a-z]+\.[a-z]+/(([\w]+/)*|([\w-]+))(#\w+)?(\?\w+=\w+(&\w+=\w+)*)?""").pattern)
                })
            }

            scenario("Produce an email string") {
                repeat(16, {
                    val url = forger.anEmail()
                    assertThat(url)
                            .matches(Regex("""[\w._\-+]+@([a-z]+\.)*[a-z]+""").pattern)
                })
            }


            scenario("Produce regex based strings") {
                val regexTable = io.kotlintest.properties.table(
                        headers("regex"),

                        // wildcard
                        row("."),
                        row(".?"),
                        row(".*"),
                        row(".+"),

                        // choice
                        row("[aeiou][tkfprs]"),
                        row("[a-h][i-p][q-z]"),
                        row("[a-h]+[i-p]*[q-z]?"),
                        row("[a-h]-[q-z]"),

                        // groups
                        row("([a-h]-[q-z])+"),
                        row("(([0-9]-)+[q-z]+\n)+"),

                        // character classes
                        row("[a-h]\\s+[q-z]"),
                        row("(\\s\\S)"),
                        row("\\w\\w \\d\\d\\d \\w\\w"),
                        row("\\w\\W / \\d\\D"),

                        // escaped characters
                        row("\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>"),

                        // quantifier range
                        row("[abc]{3}-[xyz]{2,9}")
                )


                io.kotlintest.properties.forAll(regexTable) { regex ->

                    val res = forger.aStringMatching(regex)
                    res should match(regex)
                }
            }

            scenario("Produce kotlin regex based strings") {
                val regexTable = io.kotlintest.properties.table(
                        headers("regex"),

                        // wildcard
                        row(Regex(""".""")),
                        row(Regex(""".?""")),
                        row(Regex(""".*""")),
                        row(Regex(""".+""")),

                        // choice
                        row(Regex("""[aeiou][tkfprs]""")),
                        row(Regex("""[a-h][i-p][q-z]""")),
                        row(Regex("""[a-h]+[i-p]*[q-z]?""")),
                        row(Regex("""[a-h]-[q-z]""")),

                        // groups
                        row(Regex("""([a-h]-[q-z])+""")),
                        row(Regex("""(([0-9]-)+[q-z]+\n)+""")),

                        // character classes
                        row(Regex("""[a-h]\s+[q-z]""")),
                        row(Regex("""(\s\S)""")),
                        row(Regex("""\w\w \d\d\d \w\w""")),
                        row(Regex("""\w\W / \d\D""")),

                        // escaped characters
                        row(Regex("""\<\(\[\{\\\^\-\=\$\!\|\]\}\)\?\*\+\.\>""")),

                        // quantifier range
                        row(Regex("""[abc]{3}-[xyz]{2,9}"""))
                )


                io.kotlintest.properties.forAll(regexTable) { regex ->

                    val res = forger.aStringMatching(regex)
                    assertThat(regex.matchEntire(res)).isNotNull()
                }
            }
        }
    }
}