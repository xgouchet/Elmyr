package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.throws
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class RegexParserSpek : Spek({
    describe("A regex parser") {
        val parser = RegexParser()
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        context("parsing invalid regex strings ") {
            val regexes = arrayOf(
                    // Character sets
                    "[abc",
                    "[Z-A]",
                    "[9-1]",
                    "[a-Z]",

                    // escape sequence
                    "\\b",
                    "\\c",
                    "\\g",
                    "\\h",
                    "\\i",
                    "\\j",
                    "\\k",
                    "\\l",
                    "\\m",
                    "\\o",
                    "\\p",
                    "\\q",
                    "\\u",
                    "\\v",
                    "\\x",
                    "\\y",
                    "\\z",
                    "\\A",
                    "\\B",
                    "\\C",
                    "\\E",
                    "\\F",
                    "\\G",
                    "\\H",
                    "\\I",
                    "\\J",
                    "\\K",
                    "\\L",
                    "\\M",
                    "\\N",
                    "\\O",
                    "\\P",
                    "\\Q",
                    "\\R",
                    "\\T",
                    "\\U",
                    "\\V",
                    "\\X",
                    "\\Y",
                    "\\Z",
                    "\\0",
                    "\\1",
                    "\\2",
                    "\\3",
                    "\\4",
                    "\\5",
                    "\\6",
                    "\\7",
                    "\\8",
                    "\\9",

                    "\\x"
            )

            regexes.forEach { regex ->

                it("fails forging string matching /$regex/") {
                    throws<IllegalStateException> {
                        parser.getFactory(regex)
                    }
                }
            }
        }

        context("parsing regex strings") {

            // Test cases inspired by
            // http://hg.openjdk.java.net/jdk7u/jdk7u6/jdk/file/8c2c5d63a17e/test/java/util/regex/TestCases.txt
            val regexes = arrayOf(
                    // simple char sequence
                    "foo",
                    "abc",

                    // Simple char sequence with Greedy quantifiers
                    "foo?",
                    "ab?c",
                    "x?y?z?",
                    "123+",
                    "ab+c",
                    "x+y+z+",
                    "123*",
                    "ab*c",
                    "x*y*z*",

                    // TODO freespace (?x)
                    // TODO No group (?:)

                    // Simple Alternation
                    "a|b",
                    "a|b|cd",
                    "a|ad",

                    // Simple char class
                    "[abc]+",

                    // Char class with ranges
                    "[a-d]+",
                    "[a-dW-Z]+",
                    "[a-d[m-p]]+",
                    "[^abc]+",
                    // TODO "[a-m&&[g-x]]", // intersection
                    // TODO "[a-m&&m-z]", // intersection
                    // TODO "[[a-m]&&[^a-c]]", // Complex intersection

                    // Char class with unescaped characters
                    "[abc-]+",
                    "[-xyz]+",
                    "[a-c&[x-z]]+",
                    "[abc^]+",
                    "[a-z.]+",
                    "[a-z$]+",

                    // Closing bracket not within character class
                    "]",
                    "abc]",

                    // escaped characters
                    "\\[\\]",
                    "\\(\\)",
                    "\\{\\}",
                    "\\<\\>",
                    "\\?\\*\\+",
                    "\\.\\|\\\\",
                    "\\^\\$",
                    "\\-\\=\\!",
                    "\\n\\t\\r\\f\\a\\e",

                    // TODO "\\00\\01\\02\\03\\04\\05\\06\\07" // octal values
                    // TODO "\\003\\015\\027\\031\\046\\054\\062\\070" // octal values
                    // TODO "\\0015\\0126\\0237\\0340" // octal values
                    // TODO "\\x0A\\x1d\\x29\\x3B\\x48\\x5c\\x60\\x7f" // hexadecimal values
                    // TODO "\\u010A\\u015d\\u0329\\u043B" // unicode values

                    // POSIX Character classes
                    // TODO "\\p{Alpha}" // = [\p{Lower}\p{Upper}]

                    // Predefined character classes
                    "\\d",
                    "\\D",
                    "\\w",
                    "\\W",
                    "\\s",
                    "\\S",

                    // dot metacharacter
                    ".",
                    "ba.",
                    ".*",

//                        // wildcard
//                        "",
//                        ".?",
//                        ".*",
//                        ".+",
//
//                        // choice
//                        "[aeiou][tkfprs]",
//                        "[a-h][i-p][q-z]",
//                        "[a-h]+[i-p]*[q-z]?",
//                        "[a-h]-[q-z]",
//                        "[*+?]",
//
//                        // groups
//                        "([a-h]-[q-z])+",
//                        "(([0-9]-)+[q-z]+\n)+",
//                        "((foo?)(ba[rz]))+",
//
//                        // character classes
//                        "[a-h]\\s+[q-z]",
//                        "(\\s\\S)",
//                        "\\w\\w \\d\\d\\d \\w\\w",
//                        "\\w\\W / \\d\\D",
//
//                        // whitespaces
//                        "\\t\\n",
//
//                        // escaped characters
//                        "\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>",
//
//                        // quantifier range
//                        "[abc]{3}-[xyz]{2,9}",
//                        "[abc]{10,}",
//
//                        // Or
//                        "abc|xyz",
//                        "foo|ba[rz]|spam|bacon",
//                        "test(foo|bar)",
//
//                        // negative choice
//                        "[^a-z]+"

                    // empty regex ?!
                    ""
            )

            regexes.forEach { regex ->

                it("forges string matching /$regex/") {
                    val factory = parser.getFactory(regex)
                    repeat(testRepeatCountSmall) {
                        val res = factory.getForgery(forge)
                        assertThat(res)
                                .overridingErrorMessage("String \"$res\" doesn't match regex /$regex/")
                                .matches(regex)

                        // println("\"$res\" matches /$regex/")
                    }
                }
            }
        }

        // endregion
    }
})