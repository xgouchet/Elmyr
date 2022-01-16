import fr.xgouchet.buildsrc.settings.commonConfig

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    id("githubWiki")
//    maven
}

dependencies {

    implementation(project(":core"))
    implementation(project(":inject"))
    implementation(libs.kotlin)

    compileOnly(libs.junit4)

    testImplementation(libs.bundles.junit5)
    testImplementation(libs.bundles.spek)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockitoKotlin)
}

commonConfig()

githubWiki {
    types = listOf(
            "fr.xgouchet.elmyr.junit4.ForgeRule"
    )
}
