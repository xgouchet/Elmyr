package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.Random
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class RandomForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery random1: Random,
        @Forgery random2: Random
    ) {
        assertThat(random1.nextInt())
                .isNotEqualTo(random2.nextInt())

        assertThat(random1.nextLong())
                .isNotEqualTo(random2.nextLong())

        assertThat(random1.nextFloat())
                .isNotEqualTo(random2.nextFloat())
    }
}
