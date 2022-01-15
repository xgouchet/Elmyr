buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("http://repo1.maven.org/maven2") }
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Gradle) }
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Dokka) }
    }

    dependencies {
        //        classpath(fr.xgouchet.buildsrc.Dependencies.ClassPaths.KtLint)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("http://repo1.maven.org/maven2") }
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Dokka) }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
