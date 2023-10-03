# Changelog

### Ongoing development 

#### `core`

- Add enum forgery through reflexive factory 
- Add reflexive factory available through injection (with `@Forgery`) 

### `1.4.0` (2023/09/26)

#### `core`

- Targets Java 17 
- Add a reflexive factory to automatically forge data classes 

### `1.3.2` (2022/05/05)

#### `core`

- Add the `Forge.shuffle(String): String` and `Forge.shuffle(Array<T>): Array<T>` methods

#### `semantics`

- Add the `semantics` library providing some human readable fake Strings generation (names, lipsum, …)

### `1.3.1` (2021/09/07)

#### `core`

 - Fix some invalid regex forgeries (e.g.: `"[^a-z]+"` was not handled properly)

#### `junit5`

- Fix an error when a `ForgeConfiguration` was missing a `ForgeConfigurator`

### `1.3.0` (2021/04/14)

#### `inject`

 - Add a listener to the `ForgeryInjector` to be notified when an injection happens
 - Allow advanced forgery injections using `@PairForgery`

#### `junit4`

 - Make the error message more verbose

#### `junit5`

 - Make the error message more verbose
 - Allow advanced forgery injections using `@PairForgery`
 - Add the forge to the global extension context store (use the `ForgeExtension.getForge(ExtensionContext)` method to retrieve it)

### `1.2.0` (2020/09/03)

#### `core`

 - Allow using the `@StringForgery` annotation to forge Strings based on Regex
 - Allow setting a size in `@StringForgery` annotation

#### `inject`

 - Allow injecting collections of primitives with `@BoolForgery`, `@IntForgery`, `@LongForgery`, `@FloatForgery`, `@DoubleForgery`, as well as `@StringForgery` and `RegexForgery`
 - Allow advanced forgery injections using `@AdvancedForgery` and `@MapForgery`
 
#### `junit5`

 - Allow injecting collections of primitives with `@BoolForgery`, `@IntForgery`, `@LongForgery`, `@FloatForgery`, `@DoubleForgery`, as well as `@StringForgery` and `RegexForgery`
 - Allow advanced forgery injections using `@AdvancedForgery` and `@MapForgery`

### `1.1.0` (2020/08/24)

#### `core`

 - Fix float and double forgeries (they sometimes returned values out of the requested range)
 
#### `inject`

 - Add default String type (i.e.: `ALPHABETICAL`) to the `@StringForgery` annotation
 
#### `spek`

 - Implement a `spekForge` helper method to add reproducibility in Spek tests

### 1.0.0 (2020/02/17)

#### `core`

 - Add Primitives and List/Set kotlin delegate forgeries
 - Create a Sequence forgery (eg: `Forge.aSequence { aString() }`)
 - Ensure gaussian forgeries stay within three time the standard deviation 

#### `inject`

 - Handle primitives fields / properties annotated with `@XxxForgery`, as well 
    as String fields/properties annotated with `@StringForgery` and `@RegexForgery`

#### `junit5`

 - Handle String forgeries injection annotated with `@StringForgery` and `@RegexForgery`

### 1.0.0-beta3 (2019/12/09)

#### `core`

 - Add the nullable value forgeries
 - Add the enum forgeries

#### `junit4`

 - Handle enums fields annotated with `@Forgery`

#### `junit5`

 - Fix the error message when a test fails
 - Handle enums fields and parameters annotated with `@Forgery`
 
#### `jvm`

 - Improve the File forgery factory

### 1.0.0-beta2 (2019/12/02)

#### `core`

 - Add the randomizeCase and substring forgeries

#### `jvm`

 - Implement File, Uri and Url forgery factories
 
### 1.0.0-beta1 (2019/11/27)
 
#### `inject`

 - Allow injecting Generics
 
#### `junit4`

 - Ensure tests are fully reproducible

#### `junit5`

 - Ensure tests are fully reproducible 
 - Handle Map Forgeries in JUnit5 methods
 - Handle nested collection forgeries in JUnit5 methods
 - Implement ``@BoolForgery` in JUnit5 methods
 - Implement primitive forgeries in JUnit5 methods
 - Make the ForgeConfiguration annotation Inherited by child classes
 
### 1.0.0-alpha5 (2019/11/18)

#### `junit5`

 - Allow JUnit5 extension to fill in collections
 
### 1.0.0-alpha3 (2019/11/15)

#### `junit5`

 - Create JUnit5 ForgeConfiguration annotation

### 1.0.0-alpha1 (2019/10/31)

 - Rewrite of the whole core Forge class
 - Split the library into artifacts 

### 0.12 (2019/09/10)

 - Add forgery to create an Android Web URL string (eg: `aWebUrl()`)
 - Add parameter to exclude enum values in `aValueFrom(MyEnum::class, exclude = listOf(MyEnum.Foo))` 

### 0.11 (2019/07/18)

 - Add forgery to create a random substring of a String (eg: `aSubstring("Hello world")`)

### 0.10.1 (2019/07/15)

 - Fix bug when detecting the OS to generate a path
 - Fix bugs in Regex generation

### 0.10 (2019/07/09)

 - Allow custom schem in Url forgery (eg: `aUrl(scheme = "wss")`)
 - Add forgery to create an Alpha Numerical string (`anAlphaNumericalString()`)
 - Add forgery to create a java.io.File Object (`aFile()`)
 - Add forgery to randomize a String's case (`randomizeCase("hello world")`)

 
### 0.9.1 (2018/11/09)

 - Handle negated character set in regex (eg : `aStringMatching("[^a-f]")`)

### 0.9 (2018/11/02)

 - Add forgery to create a map (`forger.aMap { aWord() to aPositiveInt()`)

### 0.8 (2018/10/25)

 - Add forgery to pick a key / value from a map (`forger.aKeyFrom(myMap)` and `forger.aValueFrom(myMap)`)
 - Sources are available from the IDE
 - Make use of kotlin.min instead of Java's Integer.min, to be compatible with all Android versions
 - Better error message in the JUnitForgerRule

### 0.7 (2018/08/23)

 - Add forgery to generate nullable with lambdas (`forger.aNullableFrom { aString() }` instead of `forger.aNullableFrom(forger.aString())`)
 - Add Forgery to generate Date (`forger.aDate()`, `forger.aPastDate()` and `forger.aFutureDate()`)
 - Update forgery to generate list with lambda (`forger.aList { aString() }` instead of `forger.aList { it.aString() }`)
 - Add forgery to generate a numerical String (`forger.aNumericalString()` and `forger.aString(StringConstraint.NUMERICAL)`) 

### 0.6 (2018/02/15)

 - Add forgery to generate a sub collection
 - Add forgery to shuffle a collection
 - Improve Email / URL / URI forgery
 - Add forgery to generate a list

### 0.5 (2017/06/15)

 - Create Strings from CharConstraint
 - Add forgery for String arrays

## 0.4 (2017/06/12)

 - Add pipe support in regex
 - Add Repeater JUnit rule
 - Add forgery for primitive arrays

## 0.3 (2017/05/29)

 - Add support for Long and Double
 - Add support for Nullable

## 0.2 (2017/05/24)

 - Kotlin delegates
 - Email and Url generation


## 0.1 (2017/05/20)

 - Initial version with basic feature
 - JUnit rule 