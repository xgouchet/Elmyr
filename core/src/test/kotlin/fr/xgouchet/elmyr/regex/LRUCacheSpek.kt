package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.throws
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class LRUCacheSpek : Spek({
    describe("A LRU cache") {

        val forge = Forge()

        beforeEachGroup {
            forge.seed = Forge.seed()
        }

        context("being initialised") {
            it("fails for capacity < 0") {
                throws<IllegalArgumentException> {
                    LRUCache<String, String>(forge.anInt(Int.MIN_VALUE, 0)) { it }
                }
            }

            it("fails for capacity == 0") {
                throws<IllegalArgumentException> {
                    LRUCache<String, String>(0) { it }
                }
            }
        }

        context("empty") {
            val capacity = forge.anInt(4, 12)
            var cache: LRUCache<String, String> = LRUCache(1) { it }
            var factoryCalls = 0

            beforeEachTest {
                factoryCalls = 0
                cache = LRUCache(capacity) { key ->
                    factoryCalls++
                    key.reversed().toLowerCase()
                }
            }

            it("calls factory on first cache miss") {
                val key = forge.anAlphabeticalString()

                val value = cache.get(key)

                assertThat(factoryCalls).isEqualTo(1)
                assertThat(value).isEqualTo(key.reversed().toLowerCase())
            }

            it("it returns cached value on second call") {
                val key = forge.anAlphabeticalString()

                val value = cache.get(key)
                factoryCalls = 0
                val value2 = cache.get(key)

                assertThat(factoryCalls).isEqualTo(0)
                assertThat(value).isEqualTo(key.reversed().toLowerCase())
                assertThat(value2).isEqualTo(value)
            }

            it("it returns cached value on second call with intermediate calls") {
                val key = forge.anAlphabeticalString()
                val otherKey = forge.aNumericalString()

                val value = cache.get(key)
                @Suppress("UNUSED_VARIABLE")
                val intermediate = cache.get(otherKey)
                factoryCalls = 0
                val value2 = cache.get(key)

                assertThat(factoryCalls).isEqualTo(0)
                assertThat(value).isEqualTo(key.reversed().toLowerCase())
                assertThat(value2).isEqualTo(value)
            }

            it("it clears cached value after multiple intermediate calls") {
                val key = forge.anAlphabeticalString()

                val value = cache.get(key)

                for (i in 0 until capacity) {
                    @Suppress("UNUSED_VARIABLE")
                    val intermediate = cache.get(forge.aNumericalString(i + 1))
                }
                factoryCalls = 0
                val value2 = cache.get(key)

                assertThat(factoryCalls).isEqualTo(1)
                assertThat(value).isEqualTo(key.reversed().toLowerCase())
                assertThat(value2).isEqualTo(value)
            }
        }
    }
})
