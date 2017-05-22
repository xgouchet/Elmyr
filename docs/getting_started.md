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
    long seed = System.nanotime()
    forger reset(seed);
```

The forger's results for the same seed are guaranteed to be always the same (as long as the calls are done in the same order). Note that forger is not concurrent-safe.

