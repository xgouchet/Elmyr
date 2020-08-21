package fr.xgouchet.elmyr.spek

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek

class ControlledFixtureForgeSpek : Spek({

    val forge = spekForge(
        seeds = mapOf(
            "ControlledFixtureForgeSpek" to 0x01L,
            "ControlledFixtureForgeSpek/A" to 0x02L,
            "ControlledFixtureForgeSpek/A/B" to 0x04L,
            "ControlledFixtureForgeSpek/A/B/C" to 0x08L,
            "ControlledFixtureForgeSpek/D" to 0x10L,
            "ControlledFixtureForgeSpek/D/E" to 0x20L
        )
    )

    var rootBg = ""
    var rootBeg = ""
    var aBg = ""
    var aBeg = ""
    var bBg = ""
    var bBet = ""
    var dBg = ""
    var dBet = ""

    beforeGroup {
        rootBg = forge.anAlphabeticalString(size = 8)
    }

    beforeEachGroup {
        rootBeg = forge.anAlphabeticalString(size = 8)
    }

    group("A") {

        beforeGroup {
            aBg = forge.anAlphabeticalString(size = 8)
        }

        beforeEachGroup {
            aBeg = forge.anAlphabeticalString(size = 8)
        }

        group("B") {

            beforeGroup {
                bBg = forge.anAlphabeticalString(size = 8)
            }

            beforeEachTest {
                bBet = forge.anAlphabeticalString(size = 8)
            }

            test("C") {
                val c = forge.anAlphabeticalString(size = 8)

                assertThat(rootBg).isEqualTo("yvbqttbp")
                assertThat(rootBeg).isEqualTo("exuqjjii")
                assertThat(aBg).isEqualTo("itvtovfu")
                assertThat(aBeg).isEqualTo("buykamok")
                assertThat(bBg).isEqualTo("tftyialg")
                assertThat(bBet).isEqualTo("mnqcssjy")
                assertThat(c).isEqualTo("oekkmgxk")
            }
        }
    }

    group("D") {

        beforeGroup {
            dBg = forge.anAlphabeticalString(size = 8)
        }

        beforeEachTest {
            dBet = forge.anAlphabeticalString(size = 8)
        }

        test("E") {
            val e = forge.anAlphabeticalString(size = 8)

            assertThat(rootBg).isEqualTo("yvbqttbp")
            assertThat(rootBeg).isEqualTo("fqcihjty")
            assertThat(dBg).isEqualTo("xeokiqvh")
            assertThat(dBet).isEqualTo("xoeiubbd")
            assertThat(e).isEqualTo("htlpnnwh")
        }
    }
})
