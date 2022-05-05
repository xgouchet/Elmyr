plugins {
    `maven-publish`
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("http://repo1.maven.org/maven2") }
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
