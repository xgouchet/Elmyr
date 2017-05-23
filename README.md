# Elmyr

> Elmyr is a Kotlin library providing tools to generate “random” values, specifically usefull for tests

[![Release](https://jitpack.io/v/xgouchet/Elmyr.svg)]
(https://jitpack.io/#xgouchet/Elmyr)

Being an adept of testing code, I write a lot of tests. The problem is that in my tests, my fake / test data always look the same. My user names are always `Bob` and `Alice`, aged `42` or `69`, with userId `4816152342` or `24601`, and eating `spam`, `eggs` and `bacon`. 

The problem is, the more test I write, the less I'm confident in my fake values, because they're always the same. 

This is where Elmyr kicks in, allowing you to create fake data based on a few constraints, making your test data random, and yet reproducible. 

## Usage

### Gradle

```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        testCompile 'com.github.xgouchet.Elmyr:library:0.1'
    }
```

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


## Documentation

The full documentation can be read on [ReadTheDocs](http://elmyr.readthedocs.io/en/latest/).

## Contributing

Contribution is fully welcome. Before submitting a Pull Request, please verify you comply with the following checklist :

 - [x] All public classes, methods and fields must be documented
 - [x] All code must be unit tested (duh…)
 - [x] All code should be useable with and without the Android SDK, from Java and Kotlin

## Release History

### 0.2 (2017/05/23)

 - Kotlin delegates


### 0.1 (2017/05/20)

 - Initial version with basic feature
 - JUnit rule 


## Meta
Xavier F. Gouchet – [@xgouchet](https://twitter.com/xgouchet)

Distributed under the MIT license. See [LICENSE.md](LICENSE.md) for more information.

[https://github.com/xgouchet/RxUnit](https://github.com/xgouchet/Elymr)
