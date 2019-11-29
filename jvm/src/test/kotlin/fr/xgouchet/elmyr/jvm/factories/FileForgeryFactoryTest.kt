package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class FileForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery file1: File,
        @Forgery file2: File
    ) {
        assertThat(file1.path)
                .isNotEqualTo(file2.path)
    }
}
