# Changelog

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