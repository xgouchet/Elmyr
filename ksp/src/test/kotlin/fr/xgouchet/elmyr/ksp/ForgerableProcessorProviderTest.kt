package fr.xgouchet.elmyr.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.config.JvmTarget
import org.junit.jupiter.api.Test

internal class ForgerableProcessorProviderTest {

    @Test
    fun ignoresClass() {
        val className = "Bar"
        val sourceContent = """
    package com.example
    class $className(
        val message: String,
        val timestamp: Long
    )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertNothingGenerated(className)
        }
    }

    @Test
    fun ignoresPrivateDataClass() {
        val className = "Foo"
        val parentClassName = "Bar"
        val sourceContent = """
    package com.example
    
    class $parentClassName {
        private data class $className(
            val message: String,
            val timestamp: Long
        )
    }
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertNothingGenerated(className)
        }
    }

    @Test
    fun ignoresProtectedDataClass() {
        val className = "Foo"
        val parentClassName = "Bar"
        val sourceContent = """
    package com.example
    
    open class $parentClassName {
        protected data class $className(
            val message: String,
            val timestamp: Long
        )
    }
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertNothingGenerated(className)
        }
    }

    @Test
    fun processDataClass() {
        val className = "Foo"
        val sourceContent = """
    package com.example
    data class $className(
        val message: String,
        val timestamp: Long
    )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Long
                    import kotlin.String
                    
                    public class FooForgeryFactory : ForgeryFactory<Foo> {
                        override fun getForgery(forge: Forge): Foo {
                            val message: String = forge.aString()
                            val timestamp: Long = forge.aLong()
                            return Foo(message = message, timestamp = timestamp)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processInternalDataClass() {
        val className = "Foo"
        val sourceContent = """
    package com.example
    internal data class $className(
        val message: String,
        val timestamp: Long
    )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Long
                    import kotlin.String
                    
                    internal class FooForgeryFactory : ForgeryFactory<Foo> {
                        override fun getForgery(forge: Forge): Foo {
                            val message: String = forge.aString()
                            val timestamp: Long = forge.aLong()
                            return Foo(message = message, timestamp = timestamp)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processNestedDataClass() {
        val className = "Foo"
        val parentClassName = "Bar"
        val sourceContent = """
    package com.example
    
    class $parentClassName {
        data class $className(
            val message: String,
            val timestamp: Long
        )
    }
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Long
                    import kotlin.String
                    
                    public class FooForgeryFactory : ForgeryFactory<Bar.Foo> {
                        override fun getForgery(forge: Forge): Bar.Foo {
                            val message: String = forge.aString()
                            val timestamp: Long = forge.aLong()
                            return Bar.Foo(message = message, timestamp = timestamp)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processDataClassWithPrimitiveArguments() {
        val className = "Spam"
        val sourceContent = """
            package com.example
            data class $className(
                val s: String,
                val b: Boolean,
                val y: Byte,
                val h: Short,
                val i: Int,
                val l: Long,
                val f: Float,
                val d: Double,
                val c: Char
            )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Boolean
                    import kotlin.Byte
                    import kotlin.Char
                    import kotlin.Double
                    import kotlin.Float
                    import kotlin.Int
                    import kotlin.Long
                    import kotlin.Short
                    import kotlin.String
                    
                    public class SpamForgeryFactory : ForgeryFactory<Spam> {
                        override fun getForgery(forge: Forge): Spam {
                            val s: String = forge.aString()
                            val b: Boolean = forge.aBool()
                            val y: Byte = forge.aByte()
                            val h: Short = forge.aShort()
                            val i: Int = forge.anInt()
                            val l: Long = forge.aLong()
                            val f: Float = forge.aFloat()
                            val d: Double = forge.aDouble()
                            val c: Char = forge.aChar()
                            return Spam(s = s, b = b, y = y, h = h, i = i, l = l, f = f, d = d, c = c)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processDataClassWithNullablePrimitiveArguments() {
        val className = "Spam"
        val sourceContent = """
            package com.example
            data class $className(
                val s: String?,
                val b: Boolean?,
                val y: Byte?,
                val h: Short?,
                val i: Int?,
                val l: Long?,
                val f: Float?,
                val d: Double?,
                val c: Char?
            )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Boolean
                    import kotlin.Byte
                    import kotlin.Char
                    import kotlin.Double
                    import kotlin.Float
                    import kotlin.Int
                    import kotlin.Long
                    import kotlin.Short
                    import kotlin.String
                    
                    public class SpamForgeryFactory : ForgeryFactory<Spam> {
                        override fun getForgery(forge: Forge): Spam {
                            val s: String? = forge.aNullable { aString() }
                            val b: Boolean? = forge.aNullable { aBool() }
                            val y: Byte? = forge.aNullable { aByte() }
                            val h: Short? = forge.aNullable { aShort() }
                            val i: Int? = forge.aNullable { anInt() }
                            val l: Long? = forge.aNullable { aLong() }
                            val f: Float? = forge.aNullable { aFloat() }
                            val d: Double? = forge.aNullable { aDouble() }
                            val c: Char? = forge.aNullable { aChar() }
                            return Spam(s = s, b = b, y = y, h = h, i = i, l = l, f = f, d = d, c = c)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processDataClassWithEnumArguments() {
        val className = "Bacon"
        val sourceContent = """
            package com.example
            
            enum class Day { MON, TUE, WED, THU, FRI, SAT, SUN }
            
            data class $className(
                val d: Day
            )
    """

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    
                    public class BaconForgeryFactory : ForgeryFactory<Bacon> {
                        override fun getForgery(forge: Forge): Bacon {
                            val d: Day = forge.getForgery()
                            return Bacon(d = d)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    @Test
    fun processDataClassWithSecondaryConst() {
        val className = "Foo"
        val sourceContent = """
            package com.example
            data class $className(
                val message: String,
                val timestamp: Long
            ) {
                constructor(obj: Any): this(obj.toString(), 0L)
            }
        """.trimIndent()

        testCompilation(
            "$className.kt",
            sourceContent
        ) {
            assertThat(it.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)

            it.assertGeneratedFactoryEquals(
                className,
                """
                    package com.example
                    
                    import fr.xgouchet.elmyr.Forge
                    import fr.xgouchet.elmyr.ForgeryFactory
                    import kotlin.Long
                    import kotlin.String
                    
                    public class FooForgeryFactory : ForgeryFactory<Foo> {
                        override fun getForgery(forge: Forge): Foo {
                            val message: String = forge.aString()
                            val timestamp: Long = forge.aLong()
                            return Foo(message = message, timestamp = timestamp)
                        }
                    }
                    
                    """.trimIndent()
            )
        }
    }

    // region Internal

    private fun testCompilation(
        testSourceFileName: String,
        testSourceFileContent: String,
        assert: (KotlinCompilation.Result) -> Unit
    ) {
        val testFile = SourceFile.kotlin(testSourceFileName, testSourceFileContent)
        val compilation = KotlinCompilation().apply {
            inheritClassPath = true
            sources = listOf(testFile)
            jvmTarget = JvmTarget.JVM_17.description
            symbolProcessorProviders = listOf(ForgerableProcessorProvider())
        }

        val result = compilation.compile()

        assert(result)
    }

    private fun KotlinCompilation.Result.assertGeneratedFactoryEquals(
        sourceClassName: String,
        expectedContent: String
    ) {
        assertThat(sourceFor("${sourceClassName}ForgeryFactory.kt"))
            .isEqualTo(expectedContent)
    }

    private fun KotlinCompilation.Result.assertNothingGenerated(
        sourceClassName: String
    ) {
        assertThat(sourceFor("${sourceClassName}ForgeryFactory.kt"))
            .isNull()
    }

    private fun KotlinCompilation.Result.sourceFor(fileName: String): String? {
        val kspGeneratedSources = getKspGeneratedSources()
        kspGeneratedSources.forEach {
            println("Found generated source ${it.name} at ${it.path}")
        }
        return kspGeneratedSources.find { it.name == fileName }
            ?.readText()
    }

    private fun KotlinCompilation.Result.getKspGeneratedSources(): List<File> {
        val workingDir = outputDirectory.parentFile
        val kspWorkingDir = workingDir.resolve("ksp")
        val kspGeneratedDir = kspWorkingDir.resolve("sources")
        val kotlinGeneratedDir = kspGeneratedDir.resolve("kotlin")
        val javaGeneratedDir = kspGeneratedDir.resolve("java")
        return kotlinGeneratedDir.walk().toList() +
            javaGeneratedDir.walk().toList()
    }

    // endregion
}
