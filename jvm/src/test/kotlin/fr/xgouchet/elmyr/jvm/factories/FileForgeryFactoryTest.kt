package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.io.File
import java.lang.IllegalStateException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class FileForgeryFactoryTest {

    @Test
    fun `factory fails on unknown OSs`(forge: Forge) {
        val factory = FileForgeryFactory()

        testFakingOsName("something") {
            assertThrows<IllegalStateException> {
                factory.getForgery(forge)
            }
        }
    }

    @Test
    fun `forges different values`(
        @Forgery file1: File,
        @Forgery file2: File
    ) {
        assertThat(file1.path)
            .isNotEqualTo(file2.path)
    }

    @Test
    fun `forge many Files`(@Forgery files: List<File>) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')
        if (files.size <= 1) return
        assumeTrue(files.size > 1)

        val absolute = files.count { it.path.startsWith("/") }
        val groups = files.groupBy { file ->
            file.path.split(File.separatorChar).first { it.isNotEmpty() }
        }

        val atleastTwoGroups = groups.size > 1
        val atLeastTwoTypes = (absolute > 0) && (absolute < files.size)

        assertThat(atleastTwoGroups || atLeastTwoTypes)
    }

    @Test
    fun `forge many Files (Linux)`(forge: Forge) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')

        testFakingOsName("Linux") {
            val files = forge.aList(64) { getForgery<File>() }

            files.forEach {
                if (it.path.startsWith('/')) {
                    val rootFolder = "/${it.path.split('/')[1]}"
                    assertThat(rootFolder)
                        .overridingErrorMessage(
                            "Expecting root folder $rootFolder to be a known Mac root folder"
                        )
                        .isIn(FileForgeryFactory.LINUX_ROOTS)
                }
            }
        }
    }

    @Test
    fun `forge many Files (Unix)`(forge: Forge) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')

        testFakingOsName("Unix") {
            val files = forge.aList(64) { getForgery<File>() }

            files.forEach {
                if (it.path.startsWith('/')) {
                    val rootFolder = "/${it.path.split('/')[1]}"
                    assertThat(rootFolder)
                        .overridingErrorMessage(
                            "Expecting root folder $rootFolder to be a known Mac root folder"
                        )
                        .isIn(FileForgeryFactory.LINUX_ROOTS)
                }
            }
        }
    }

    @Test
    fun `forge many Files (AIX)`(forge: Forge) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')

        testFakingOsName("AIX") {
            val files = forge.aList(64) { getForgery<File>() }

            files.forEach {
                if (it.path.startsWith('/')) {
                    val rootFolder = "/${it.path.split('/')[1]}"
                    assertThat(rootFolder)
                        .overridingErrorMessage(
                            "Expecting root folder $rootFolder to be a known Mac root folder"
                        )
                        .isIn(FileForgeryFactory.LINUX_ROOTS)
                }
            }
        }
    }

    @Test
    fun `forge many Files (Mac OS X)`(forge: Forge) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')

        testFakingOsName("Mac OS X") {
            val files = forge.aList(64) { getForgery<File>() }

            files.forEach {
                if (it.path.startsWith('/')) {
                    val rootFolder = "/${it.path.split('/')[1]}"
                    assertThat(rootFolder)
                        .overridingErrorMessage(
                            "Expecting root folder $rootFolder to be a known Mac root folder"
                        )
                        .isIn(FileForgeryFactory.MAC_ROOTS)
                }
            }
        }
    }

    @Test
    fun `forge many Files (Windows)`(forge: Forge) {
        // assuming we're on unix
        assumeTrue(File.separatorChar == '/')

        testFakingOsName("Windows 10") {
            val files = forge.aList(64) { getForgery<File>() }

            files.forEach {
                if (!it.path.startsWith('.')) {
                    assertThat(it.path)
                        .matches("[A-Z]:\\\\.*")
                }
            }
        }
    }

    private fun testFakingOsName(osName: String, test: () -> Unit) {
        val realOsName = System.getProperty("os.name")
        System.setProperty("os.name", osName)
        test()
        System.setProperty("os.name", realOsName)
    }
}
