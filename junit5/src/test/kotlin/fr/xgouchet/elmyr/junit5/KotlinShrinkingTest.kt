package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.junit5.shrink.Shrink
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
internal class KotlinShrinkingTest {

    @Shrink(maximumRunCount = RUN_COUNT_NO_PARAMS)
    @TestTemplate
    fun testNoParams() {
        runCountNoParams++
    }

    @Shrink(maximumRunCount = RUN_COUNT_WITH_PARAMS)
    @TestTemplate
    fun testWithParams(@IntForgery i: Int) {
        runCountWithParams.add(i)
    }

    @Shrink(maximumRunCount = RUN_COUNT_FAILING)
    @TestTemplate
    fun testFailing(@IntForgery(0, 65536) i: Int) {
        runCountFailing++

        if ((i in 42..666) || (i in 34605..44523)) assertThat(i).isLessThan(0)
    }

    companion object {

        @AfterAll
        @JvmStatic
        fun `it's just another brick in the wall`() {
            assertThat(runCountNoParams).isEqualTo(RUN_COUNT_NO_PARAMS)
            assertThat(runCountFailing).isEqualTo(RUN_COUNT_FAILING)

            assertThat(runCountWithParams.size).isEqualTo(RUN_COUNT_WITH_PARAMS)
            assertThat(runCountWithParams.toSet().size).isGreaterThan(RUN_COUNT_WITH_PARAMS / 3)
        }

        const val RUN_COUNT_NO_PARAMS = 11
        const val RUN_COUNT_WITH_PARAMS = 13
        const val RUN_COUNT_FAILING = 1024

        var runCountNoParams = 0
        var runCountFailing = 0
        var runCountWithParams = mutableListOf<Int>()
        var runCountFlaky = mutableListOf<Int>()
    }
}
