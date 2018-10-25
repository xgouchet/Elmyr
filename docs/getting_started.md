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
    testCompile 'com.github.xgouchet:Elmyr:0.8'
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
	</repositories>
```

Then, add the dependency : 

```xml
	<dependency>
	    <scope>test</scope>
	    <groupId>com.github.xgouchet</groupId>
	    <artifactId>Elmyr</artifactId>
	    <version>0.8</version>
	</dependency>
```

If you need Elmyr in your production code (and not just in your tests), remove the `<scope>` node.

### Using Elmyr to forge data

#### JUnit

**Elmyr** provides a JUnit Rule that you can use to make your tests reproducible. 

```java
public class FooTest {
    
    @Rule public JUnitForger forger = new JUnitForger();
    
    @Test
    public void shouldDoSomething(){
        int base = forger.aPositiveInt();
        int result = square(base);
        assertThat(result).isEqualTo(base * base);
    }
}
```

If a test using the `JUnitForger` rule fails, the log displays the following line : 

> ‘shouldDoSomething’ failed with fake seed = 0x4815162342

With that information, you can override the forger's seed to reproduce the exact same test steps by using the following 
`@Before` method : 

```java
    @Before
    public void resetForger() {
        forger.reset(0x4815162342);
    }
```

#### Just the Forger

Of course you can use Elmyr's `Forger` class in other situations. You can simply instantiate it like this : 

```java
    Forger forger = new Forger();
    long seed = System.nanoTime()
    forger reset(seed);
```

The FORGER's results for the same seed are guaranteed to be always the same (as long as the calls are done in the same order). Note that forger is not concurrent-safe.

#### Kotlin

Because Kotlin is Fun™, Elmyr provides some delegates for read-only properties, to forge data in an easy way : 

```kotlin
    val lipsum : String by forgeryWithConstraint(StringConstraint.SENTENCE)
    val id : String by forgeryWithRegex(Regex("""[A-Z]+-[\d]+""")
    val number : Int by forgeryWithRange(0, 42)
    val rate : Float by forgeryWithDistribution(1.0f, 0.5f)
```

Each of those method accept additional parameters for furter configuration, and also a parameter named forger to provide a seeded Forger. 

```kotlin
    val number : Int by forgeryWithConstraint(IntConstraint.TINY, forger = myForger)
```






