# Elmyr

> Elmyr is a Kotlin library providing tools to generate “random” values, specifically usefull for tests

Before, I used to write my tests like this

```java
    public void shouldDoSomething(){
        Foo foo = mock(Foo.class);
        when(foo.getId()).thenReturn(42);
    
        int result = testedBar.getFooId(foo);
    
        assertThat(result).isEqualTo(42);
    }
```

The problem is, when all your fake ints are `42`, `666`, `1337`, `4815162342` or `24601`, and all your fake Strings are
`“foo”`, `“bar”`, `“baz”`, or `“kamoulox”`, how can you be sure that your tests really work ?

This is where `Elmyr` comes into play. It provides a [JUnit Rule](https://github.com/junit-team/junit4/wiki/rules), as
well as independant Forger classes that allows you to generate various data.

Now, the above test can be rewritten as :

```java
    public void shouldDoSomething(){
        int fakeId = forger.anInt();
        Foo foo = mock(Foo.class);
        when(foo.getId()).thenReturn(fakeId);
    
        int result = testedBar.getFooId(foo);
    
        assertThat(result).isEqualTo(fakeId);
    }
```

## Usage

### Gradle

To add this library to your build, add the following lines to your app's build.gradle :

```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        testCompile 'com.github.xgouchet.Elmyr:library:0.1'
    }
```

If you need **Elmyr** in your production code (and not just in your tests), replace `testCompile` with `compile`

### JUnit

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

## Documentation

I've yet to add an online documentation, but all public classes and methods are fully documented.

## Contributing

Contribution is fully welcome. Before submitting a Pull Request, please verify you comply with the following checklist :

 - [x] All public classes, methods and fields must be documented
 - [x] All code must be unit tested (duh…)
 - [x] All code should be useable with and without the Android SDK, from Java and Kotlin

## Release History

 - 0.1
     - Initial version with basic feature
     - JUnit rule 


## Meta
Xavier F. Gouchet – [@xgouchet](https://twitter.com/xgouchet)

Distributed under the MIT license. See [LICENSE.md](LICENSE.md) for more information.

[https://github.com/xgouchet/RxUnit](https://github.com/xgouchet/Elymr)
