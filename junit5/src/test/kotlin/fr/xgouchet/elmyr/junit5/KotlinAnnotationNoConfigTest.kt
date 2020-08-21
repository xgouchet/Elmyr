package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
internal open class KotlinAnnotationNoConfigTest {

    // region Forge

    @Test
    fun testRun1(@Forgery forge: Forge) {
        checkSeedChanged(forge)
    }

    @Test
    fun testRun2(@Forgery forge: Forge) {
        checkSeedChanged(forge)
    }

    @Test
    fun testRun3(forge: Forge) {
        checkSeedChanged(forge)
    }

    // endregion

    // region Internal

    private fun checkSeedChanged(forge: Forge) {
        val previousSeed = memoizedSeed
        if (previousSeed != null) {
            Assertions.assertThat(forge.seed).isNotEqualTo(previousSeed)
        }
        memoizedSeed = forge.seed
    }

    // endregion

    companion object {
        internal var memoizedSeed: Long? = null
    }
}
