import fr.xgouchet.buildsrc.Dependencies
import fr.xgouchet.buildsrc.settings.commonConfig
import fr.xgouchet.buildsrc.testCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    id("githubWiki")
    jacoco
    maven
}

dependencies {

    compile(Dependencies.Libraries.Kotlin)

    testCompile(Dependencies.Libraries.JUnit5)
    testCompile(Dependencies.Libraries.Spek)
    testCompile(Dependencies.Libraries.TestTools)
}

commonConfig(0.925)

githubWiki {
    types = listOf(
        "fr.xgouchet.elmyr.Forge",
        "fr.xgouchet.elmyr.ForgeryAware",
        "fr.xgouchet.elmyr.ForgeConfigurator",
        "fr.xgouchet.elmyr.ForgeryFactory",
        "fr.xgouchet.elmyr.Case",
        "fr.xgouchet.elmyr.kotlin.BooleanProperty",
        "fr.xgouchet.elmyr.kotlin.IntProperty",
        "fr.xgouchet.elmyr.kotlin.LongProperty",
        "fr.xgouchet.elmyr.kotlin.FloatProperty",
        "fr.xgouchet.elmyr.kotlin.DoubleProperty",
        "fr.xgouchet.elmyr.kotlin.NullableProperty",
        "fr.xgouchet.elmyr.kotlin.FactoryProperty",
        "fr.xgouchet.elmyr.kotlin.FactoryListProperty",
        "fr.xgouchet.elmyr.kotlin.FactorySetProperty",
        "fr.xgouchet.elmyr.annotation.AdvancedForgery",
        "fr.xgouchet.elmyr.annotation.BoolForgery",
        "fr.xgouchet.elmyr.annotation.DoubleForgery",
        "fr.xgouchet.elmyr.annotation.FloatForgery",
        "fr.xgouchet.elmyr.annotation.Forgery",
        "fr.xgouchet.elmyr.annotation.IntForgery",
        "fr.xgouchet.elmyr.annotation.LongForgery",
        "fr.xgouchet.elmyr.annotation.MapForgery",
        "fr.xgouchet.elmyr.annotation.RegexForgery",
        "fr.xgouchet.elmyr.annotation.StringForgery",
        "fr.xgouchet.elmyr.annotation.StringForgeryType"
    )
}
