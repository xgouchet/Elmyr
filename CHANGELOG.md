# Changelog

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