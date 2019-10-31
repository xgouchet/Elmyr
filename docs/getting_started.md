## Getting Started

This document will show you how to include Elmyr and use it in your Java or Kotlin project. 

### Import Elmyr in your project

#### Using Gradle

To add this library to your build, first add the repository to your project's `build.gradle`. 

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the dependency in your app's build.gradle :

```groovy
dependencies {
    testCompile("com.github.xgouchet.Elmyr:core:x.x.x")
}
```

If you need Elmyr in your production code (and not just in your tests), replace `testCompile` with `compile`.

If needed you can also use other artifacts, by replacing `core` with the artifact name (eg : `junit4`, `junit5`, `jvm`, more on this below).

#### Using Maven

To add this library, add the following repository to your `pom.xml` :

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Then, add the dependency : 

```xml
	<dependency>
	    <scope>test</scope>
	    <groupId>com.github.xgouchet.Elmyr</groupId>
	    <artifactId>core</artifactId>
	    <version>x.x.x</version>
	</dependency>
```

If you need Elmyr in your production code (and not just in your tests), remove the `<scope>` node.

If needed you can also use other artifacts, by replacing `core` with the artifact name (eg : `junit4`, `junit5`, `jvm`, more on this below).

### Artifacts Overview

#### `core`

This is the core module of Elmyr, providing the base utilities to forge data for your tests. It is required by all the other modules

#### [`junit4`](Junit4.md)

This artifact provides a JUnit4 Rule that simplify the process of writing JUnit4 style tests with Elmyr. 

#### [`junit5`](Junit5.md)

This artifact provides a JUnit5 Extension that simplify the process of writing JUnit4 style tests with Elmyr. 

#### [`jvm`](JVM.md)

This artifact implements a bunch of ForgeryFactory to generate random standard from the java standard library.