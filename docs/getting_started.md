## Getting Started

This document will show you how to include Elmyr and use it in your Java or Kotlin project. 

### Import Elmyr in your project

#### Using Gradle

To add this library to your build, add the following lines to your app's build.gradle :

```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        testCompile 'com.github.xgouchet:Elmyr:0.1.1'
    }
```

If you need Elmyr in your production code (and not just in your tests), replace `testCompile` with `compile`. 

#### Using Maven

To add this library, add the following repository to your `pom.xml` :

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>Copy
```

Then, add the dependency : 

```xml
	<dependency>
	    <groupId>com.github.xgouchet</groupId>
	    <artifactId>Elmyr</artifactId>
	    <version>0.1.1</version>
	</dependency>
```



