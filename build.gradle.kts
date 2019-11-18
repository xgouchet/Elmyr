buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Gradle) }
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Dokka) }
        jcenter()
    }

    dependencies {
        //        classpath(fr.xgouchet.buildsrc.Dependencies.ClassPaths.KtLint)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Dokka) }
        jcenter()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
