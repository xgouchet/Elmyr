import java.io.File
import java.net.URI
import java.util.Properties

plugins {
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://repo1.maven.org/maven2") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://dl.bintray.com/kotlin/dokka") }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://repo1.maven.org/maven2") }
        maven { setUrl("https://dl.bintray.com/kotlin/dokka") }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

group = "fr.xgouchet.elmyr"
val localPropertiesFile: File = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    val p = Properties()
    localPropertiesFile.inputStream().use { fis ->
        p.load(fis)
    }
    p.forEach { k, v ->
        project.ext[k.toString()] = v
    }
}

nexusPublishing {
    this.repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            val sonatypeUsername = project.properties["ossrhUsername"]?.toString()
            val sonatypePassword = project.properties["ossrhPassword"]?.toString()
            if (sonatypeUsername != null) username.set(sonatypeUsername)
            if (sonatypePassword != null) password.set(sonatypePassword)
        }
    }
}
