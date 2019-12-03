# Elmyr

> Elmyr is a Kotlin library providing tools to generate “random” values, specifically usefull for tests

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
[![Kotlin 1.2.10](https://img.shields.io/badge/Kotlin-1.3.0-blue.svg)](http://kotlinlang.org)

[![Release](https://jitpack.io/v/xgouchet/Elmyr.svg)](https://jitpack.io/#xgouchet/Elmyr)
[![Build Status](https://travis-ci.org/xgouchet/Elmyr.svg?branch=master)](https://travis-ci.org/xgouchet/Elmyr)
[![codecov](https://codecov.io/gh/xgouchet/Elmyr/branch/master/graph/badge.svg)](https://codecov.io/gh/xgouchet/Elmyr)



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
class FoonTest {

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
open class FooTest {

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

## Documentation

The full documentation will be comming shortly

## Contributing 

Contribution is fully welcome. Before submitting a Pull Request, please verify you comply with the following checklist :

 - [x] All public classes, methods and fields must be documented
 - [x] All code must be unit tested (duh…)
 - [x] All code should be useable with and without the Android SDK, from Java and Kotlin

## Release History

### Latest Release: `1.0.0-beta2` (2019/12/02)

#### `core`

 - Add the randomizeCase and substring forgeries

#### `jvm`

 - Implement File, Uri and Url forgery factories

For more information, read the [Changelog](CHANGELOG.md).


## Meta

Xavier F. Gouchet – [@xgouchet](https://twitter.com/xgouchet)

Distributed under the MIT license. See [LICENSE.md](LICENSE.md) for more information.

[https://github.com/xgouchet/Elymr](https://github.com/xgouchet/Elymr)
