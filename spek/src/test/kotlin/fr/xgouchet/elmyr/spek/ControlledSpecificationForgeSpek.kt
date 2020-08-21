package fr.xgouchet.elmyr.spek

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ControlledSpecificationForgeSpek : Spek({

    val forge = spekForge(
        seeds = mapOf(
            "ControlledSpecificationForgeSpek" to 0x101L,
            "ControlledSpecificationForgeSpek/A" to 0x102L,
            "ControlledSpecificationForgeSpek/A/B" to 0x103L,
            "ControlledSpecificationForgeSpek/A/B/C" to 0x104L,
            "ControlledSpecificationForgeSpek/D" to 0x105L,
            "ControlledSpecificationForgeSpek/D/E" to 0x106L
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

    describe("A") {

        beforeGroup {
            aBg = forge.anAlphabeticalString(size = 8)
        }

        beforeEachGroup {
            aBeg = forge.anAlphabeticalString(size = 8)
        }

        context("B") {

            beforeGroup {
                bBg = forge.anAlphabeticalString(size = 8)
            }

            beforeEachTest {
                bBet = forge.anAlphabeticalString(size = 8)
            }

            it("C") {
                val c = forge.anAlphabeticalString(size = 8)

                assertThat(rootBg).isEqualTo("rpuflqtb")
                assertThat(rootBeg).isEqualTo("lydllqad")
                assertThat(aBg).isEqualTo("shswamee")
                assertThat(aBeg).isEqualTo("fbkkwnjy")
                assertThat(bBg).isEqualTo("lvyhytto")
                assertThat(bBet).isEqualTo("xewstfbm")
                assertThat(c).isEqualTo("spdlojoh")
            }
        }
    }

    describe("D") {

        beforeGroup {
            dBg = forge.anAlphabeticalString(size = 8)
        }

        beforeEachTest {
            dBet = forge.anAlphabeticalString(size = 8)
        }

        it("E") {
            val e = forge.anAlphabeticalString(size = 8)

            assertThat(rootBg).isEqualTo("rpuflqtb")
            assertThat(rootBeg).isEqualTo("bvgtquxk")
            assertThat(dBg).isEqualTo("froxmvkk")
            assertThat(dBet).isEqualTo("vxoxxkjy")
            assertThat(e).isEqualTo("ggaoesre")
        }
    }
})
