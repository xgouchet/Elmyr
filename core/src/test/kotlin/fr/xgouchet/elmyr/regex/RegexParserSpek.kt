package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.throws
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class RegexParserSpek : Spek({
    describe("A regex parser") {
        val parser = RegexParser()
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        context("parsing invalid regex strings ") {
            val regexes = arrayOf(
                // Character class
                "[abc",
                "[Z-A]",
                "[9-1]",
                "[a-Z]",
                "[a-z",
                "[a-",
                "[]",
                "(a)[\\1]",

                // repetition
                "a{}",
                "a{-4}",
                "a{5,2}",
                "a{12",
                "a{,2}",

                // invalid escaped sequences
                "\\088",
                "\\xf8e75",
                "\\u8ab58",

                // groups
                "(ab",
                "ab)",
                "(ab(",

                // back reference
                "a*bc\\1",
                "(\\w)(\\d+(\\2))",

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
                "a\\"
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

                // Repetition
                "[\\w]{5}",
                "[0-9]{3,}",
                "[a-z]{2,7}",
                "[\\w]{42}",
                "[0-9]{13,}",
                "[a-z]{23,42}",
                "[a-z]{2,2}",

                // Embedded flags : (?α) = turn on; (?-α) turn of; (?α-κ:…) non capturing with flag
                // TODO case insensitive (?i)
                // TODO Unix Lines (?d)
                // TODO multiline (?m)
                // TODO dotall (?s)
                // TODO unicode case (?u)
                // TODO freespace (?x)
                // TODO No group (?:)

                // Simple Alternation
                "a|b",
                "a|b|cd",
                "a|ad",
                "foo|ba[rz]|spam|bacon",

                // Simple char class
                "[abc]+",

                // Character class with ranges
                "[a-d]+",
                "[a-dW-Z]+",
                // TODO nested range "[a-d[m-p]]+",
                "[^abc]+",
                "[^a-zA-Z0-9:\\-_.@$]+",
                // TODO fix flakyness "[^\\w\\d]+",
                // TODO fix flakyness "[^\\d]+",
                // TODO #57 Add support for Character Class Intersection

                // Char class with escaped characters
                "[a\\-z]+",
                "[\\]]+",

                // Char class with unescaped characters
                "[abc-]+",
                "[-xyz]+",
                // TODO nested range "[a-c&[x-z]]+",
                "[abc\\^]+",
                "[a-z.]+",
                "[a-z$]+",
                "[^\\^]+",

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

                // TODO #58 Add support for octal, hexadecimal and unicode escape
                "\\00\\01\\02\\03\\04\\05\\06\\07", // octal values
                "\\015\\027\\031\\046\\054\\062\\070", // octal values
                "\\0126\\0237\\0340", // octal values
                "[\\0101-\\0132]+", // nested octal

                "\\x0a\\x1d\\x29\\x3b\\x48\\x5c\\x60\\x7f", // hexadecimal values
                "[\\x61-\\x7a]+", // nested hexadecimal
                "\\u010a\\u015d", // unicode values
                "[\\u0061-\\u007a]+", // nested unicode

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
                ".?",
                ".*",
                ".+",

                // groups
                "(ab)+",
                "(a+b)+",
                "(a|b)+",
                "(a|ba(r|z))+",
                "(ab)*",
                "(ab)(cd*)",
                "abc(d)*abc",
                "([a-h]-[q-z])+",
                "(([0-9]-)+[q-z]+\n)+",
                "((foo?)(ba[rz]))+",

                // Back references
                "(a*)bc\\1",
                "(a((b)(c))(d))\\1\\2\\3\\4\\5",
                "(\\d)(b)(c)(d)(\\d)(f)(g)(h)(\\d)(j)(k)(l)(m)(n)(\\d)(p)(q)(r)(s)(t)(\\d)(v)(w)(x)(y)(z)\\|\\1\\5\\9\\15\\21",
                "(\\w)(\\d+(\\1))",

                // Complex use cases
                "<([a-z]+)>([^<]*)</\\1>",

                // empty regex ?!
                ""
            )

            regexes.forEach { regex ->

                it("forges string matching /$regex/") {
                    val factory = parser.getFactory(regex) as RegexStringFactory

                    assertThat(factory.rootNode.toRegex())
                        .overridingErrorMessage(
                            "Node ${factory.rootNode} doesn't match expected regex:\n" +
                                    "expected:<\"/$regex/\"> " +
                                    "but was:<\"${factory.rootNode.toRegex()}\">"
                        )
                        .isEqualTo("/$regex/")
                    repeat(testRepeatCountSmall) {
                        val res = factory.getForgery(forge)
                        assertThat(res)
                            .overridingErrorMessage("String \"$res\" doesn't match regex /$regex/")
                            .matches(regex)
                    }
                }
            }
        }

        // endregion
    }
})
