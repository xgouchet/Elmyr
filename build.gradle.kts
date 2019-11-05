buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl(fr.xgouchet.buildsrc.Dependencies.Repositories.Gradle) }
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
        jcenter()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
