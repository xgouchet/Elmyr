package fr.xgouchet.elmyr.spek

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ControlledGherkinForgeSpek : Spek({

    val forge = spekForge(
        seeds = mapOf(
            "ControlledGherkinForgeSpek" to 0x100L,
            "ControlledGherkinForgeSpek/Feature: A" to 0x200L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: B" to 0x300L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: B/When: C" to 0x400L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: B/Then: D" to 0x500L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: B/Then: E" to 0x600L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: GWT" to 0x700L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: GWT/Given: X" to 0x800L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: GWT/When: Y" to 0x900L,
            "ControlledGherkinForgeSpek/Feature: A/Scenario: GWT/Then: Z" to 0xA00L
        )
    )

    var rootBg = ""
    var rootBeg = ""
    var aBf = ""
    var aBes = ""
    var bBs = ""
    var bBes = ""
    var c = ""
    var gwtBs = ""
    var gwtBes = ""
    var x = ""
    var y = ""

    beforeGroup {
        rootBg = forge.anAlphabeticalString(size = 8)
    }

    beforeEachGroup {
        rootBeg = forge.anAlphabeticalString(size = 8)
    }

    Feature("A") {

        beforeFeature {
            aBf = forge.anAlphabeticalString(size = 8)
        }

        beforeEachScenario {
            aBes = forge.anAlphabeticalString(size = 8)
        }

        Scenario("B") {

            beforeScenario {
                bBs = forge.anAlphabeticalString(size = 8)
            }

            beforeEachStep {
                bBes = forge.anAlphabeticalString(size = 8)
            }

            When("C") {
                c = forge.anAlphabeticalString(size = 8)
            }

            Then("D") {
                val d = forge.anAlphabeticalString(size = 8)

                assertThat(rootBg).isEqualTo("ophbyach")
                assertThat(rootBeg).isEqualTo("xxdognji")
                assertThat(aBf).isEqualTo("wjobwpig")
                assertThat(aBes).isEqualTo("aedchjdx")
                assertThat(bBs).isEqualTo("vxobxffm")
                assertThat(bBes).isEqualTo("loqdjgai")
                assertThat(c).isEqualTo("lmfypvwt")
                assertThat(d).isEqualTo("slubjyph")
            }

            Then("E") {
                val e = forge.anAlphabeticalString(size = 8)

                assertThat(rootBg).isEqualTo("ophbyach")
                assertThat(rootBeg).isEqualTo("xxdognji")
                assertThat(aBf).isEqualTo("wjobwpig")
                assertThat(aBes).isEqualTo("aedchjdx")
                assertThat(bBs).isEqualTo("vxobxffm")
                assertThat(bBes).isEqualTo("bekqtsvp")
                assertThat(c).isEqualTo("lmfypvwt")
                assertThat(e).isEqualTo("xtjepivh")
            }
        }

        Scenario("GWT") {
            beforeScenario {
                gwtBs = forge.anAlphabeticalString(size = 8)
            }

            beforeEachStep {
                gwtBes = forge.anAlphabeticalString(size = 8)
            }

            Given("X") {
                x = forge.anAlphabeticalString(size = 8)
            }

            When("Y") {
                y = forge.anAlphabeticalString(size = 8)
            }

            Then("Z") {
                val z = forge.anAlphabeticalString(size = 8)

                assertThat(rootBg).isEqualTo("ophbyach")
                assertThat(rootBeg).isEqualTo("ugmqvhof")
                assertThat(aBf).isEqualTo("wjobwpig")
                assertThat(aBes).isEqualTo("vrweanlf")
                assertThat(gwtBs).isEqualTo("syxqtnas")
                assertThat(gwtBes).isEqualTo("ljaeiymp")
                assertThat(x).isEqualTo("mufnhxae")
                assertThat(y).isEqualTo("oupkwxme")
                assertThat(z).isEqualTo("tnxisocm")
            }
        }
    }
})
