package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ReflexiveFactorySpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        context("forging unknown data class ") {

            it("cannot forge a non data class") {
                try {
                    forge.getForgery<NotADataClass>()
                    throw AssertionError("Should fail here")
                } catch (e: ForgeryException) {
                    // Nothing to do here
                }
            }

            it("forges data class instance with primitive fields") {
                val withPrimitiveFields = forge.getForgery<WithPrimitiveFields>()

                assertThat(withPrimitiveFields.anInt).isNotZero()
                assertThat(withPrimitiveFields.aLong).isNotZero()
                assertThat(withPrimitiveFields.aFloat).isNotZero()
                assertThat(withPrimitiveFields.aDouble).isNotZero()
                assertThat(withPrimitiveFields.aString).isNotEmpty()
            }

            it("forges data class instance with data class fields") {
                val withDataClassField = forge.getForgery<WithDataClassField>()

                val withPrimitiveFields = withDataClassField.field
                assertThat(withPrimitiveFields.anInt).isNotZero()
                assertThat(withPrimitiveFields.aLong).isNotZero()
                assertThat(withPrimitiveFields.aFloat).isNotZero()
                assertThat(withPrimitiveFields.aDouble).isNotZero()
                assertThat(withPrimitiveFields.aString).isNotEmpty()
            }

            it("forges data class instance with collection fields") {
                val withCollectionFields = forge.getForgery<WithCollectionFields>()

                assertThat(withCollectionFields.floatList).isNotEmpty()
                assertThat(withCollectionFields.stringToLongMap).isNotEmpty()
                assertThat(withCollectionFields.dataClassSet).isNotEmpty()
            }
        }
    }
})

class NotADataClass(val i: Int)

data class WithPrimitiveFields(
    val aBool: Boolean,
    val anInt: Int,
    val aLong: Long,
    val aFloat: Float,
    val aDouble: Double,
    val aString: String
)

data class WithDataClassField(val field: WithPrimitiveFields)

data class WithCollectionFields(
    val floatList: List<Float>,
    val stringToLongMap: Map<String, Long>,
    val dataClassSet: Set<WithPrimitiveFields>
)
