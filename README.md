# Elmyr

> Elmyr is a Kotlin library providing tools to generate “random” values, specifically usefull for tests


[![Release](https://jitpack.io/v/xgouchet/Elmyr.svg)](https://jitpack.io/#xgouchet/Elmyr)
[![Documentation Status](https://img.shields.io/badge/docs-wiki-brightgreen.svg)](https://github.com/xgouchet/Elmyr/wiki)

[![Build Status](https://travis-ci.org/xgouchet/Elmyr.svg)](https://travis-ci.org/xgouchet/Elmyr)
[![codecov](https://codecov.io/gh/xgouchet/Elmyr/branch/master/graph/badge.svg)](https://codecov.io/gh/xgouchet/Elmyr)

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://paypal.me/xaviergouchet)

Being an adept of testing code, I write a lot of tests. One thing I noticed is that in my tests, my fake / test data always look the same. My user names are always `“Bob”` and `“Alice”`, aged `42` or `69`, with userId `4816152342` or `24601`, and eating `“spam”`, `“eggs”` and `“bacon”`. 

The problem is, the more test I write, the less I'm confident in my fake values, because they're always the same. 

This is where Elmyr kicks in, allowing you to create fake/fuzzy data based on a few constraints, making your test data random, and yet reproducible. 

## Usage

### Gradle

```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        testCompile("com.github.xgouchet.Elmyr:core:x.x.x")
        testCompile("com.github.xgouchet.Elmyr:junit4:x.x.x")
        testCompile("com.github.xgouchet.Elmyr:junit5:x.x.x")
        testCompile("com.github.xgouchet.Elmyr:jvm:x.x.x")
    }
```

### Forging data: the `core` module

You can create an instance of the `Forge` class, and from that generate: 

 - primitives, with basic constraints
 - Strings matching simple predicates or even Regexes
 - Your own custom data, by implementing the `ForgeryFactory` interface, then
    calling the `Forge::addFactory` method.

### ForgeRule for `junit4`

You can instantiate a `ForgeRule` instance, which extends the `Forge` class,
add factories to it, and then annotate fields on your test class with `@Forgery`.

```kotlin
class FooTest {

    @Rule
    @JvmField
    val forge = ForgeRule()
            .withFactory(FooFactory())
            .withFactory(BarFactory())

    @Forgery
    internal lateinit var fakeBar: Bar

    @Forgery
    lateinit var fakeFooList: List<Foo>

    //…
}
```

### ForgeExtension for `junit5`

You can add an extension and configure it. In addition to creating forgeries on 
fields/properties of your test class, you can inject parameters directly on your 
test methods.

```kotlin
@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(KotlinAnnotationTest.Configurator::class)
internal class FooTest {

    @Forgery
    internal lateinit var fakeBar: Bar

    @Forgery
    lateinit var fakeFooList: List<Foo>

    @Test
    fun testSomething(@IntForgery i: Int, forge:Forge){
        // …
    }
}
```



### `spek` forgeries

You can create a custom Forge instance with `spekForge` to be able to 
add reproducibility to Spek tests.

```kotlin
class CalculatorSpek : Spek({

    val forge = spekForge(
        seeds = mapOf(
            "CalculatorSpek/A calculator/addition/returns the sum of its arguments" to 0x1337L
        )
    )

    describe("A calculator") {
        val calculator by memoized { Calculator() }
        
        describe("addition") {
            it("returns the sum of its arguments") {
                val a = forge.anInt()
                val b = forge.anInt()
                assertEquals(calculator.add(a, b), a + b)
            }
        }
    }
})
```

## Documentation

The full documentation will be coming shortly

## Contributing 

Contribution is fully welcome. Before submitting a Pull Request, please verify you comply with the following checklist :

 - [x] All public classes, methods and fields must be documented
 - [x] All code must be unit tested (duh…)
 - [x] All code should be usable with and without the Android SDK, from Java and Kotlin

## Release History

### Latest Release: `1.0.0` (2020/02/17)

#### `core`

 - Add the List/Set kotlin delegate forgeries
 - Ensure gaussian forgeries stay within three time the standard deviation 

#### `inject`

 - Handle primitives fields / properties annotated with `@XxxForgery`, as well 
    as String fields/properties annotated with `@StringForgery` and `@RegexForgery`

#### `junit5`

 - Handle String forgeries injection annotated with `@StringForgery` and `@RegexForgery`


## Donate

This library is completely free to use and modify (as per the [License](LICENSE.md)). 
I try my best to make it as good as possible, but only do this on my free time. 
If you want to support my work, you can click the Donate button below.

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://paypal.me/xaviergouchet)

## Meta

Xavier F. Gouchet – [@xgouchet](https://twitter.com/xgouchet)

Distributed under the MIT license. See [LICENSE.md](LICENSE.md) for more information.

[https://github.com/xgouchet/Elymr](https://github.com/xgouchet/Elymr)
