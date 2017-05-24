## Forger

The `Forger` class is the main class of the Elmyr library. 

Note that this documentation use the Kotlin syntax, but it's transparent for Java users. Note that parameters with default values are optionnal. 

### Resetting the seed

Like many random generators, the Forger class is based on a seed, which is a Long number. You can re-set the seed using the `reset` method.

 - **fun reset(seed : Long)**

    Resets this forger with the given seed. Knowing the seed allow the forger to reproduce previous data.

    - _seed_ the seed to use (try and remember to be able to reproduce a forgery).


---

### Generating Booleans

With Elmyr, you can forge Boolean values (ie : `true` or `false`). 

 - **fun aBool(probability : Float = 0.5f) : Bool**

    Returns a random boolean, with a biased probability of returning `true`.

    - _probability_ : the probability for the result to be `true`. _0.0f_ means always `false`, _1.0f_ means always `true`.


---

### Generating Ints

With Elmyr, you can forge integer values (eg: `42`, `24601`, `4815162342`, …).

 - **fun anInt(constraint: IntConstraint = IntConstraint.ANY): Int**

    Returns a random int, based on a constraint. 

    - _constraint_ : the constraint to build the int. For more information, read the [Constraints](constraints.html) page


 - **fun anInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int**

    Returns a random int, within the given range. 
    
    - _min_ : the minimum value (inclusive).
    - _max_ : the maximum value (exclusive).


 - **fun aPositiveInt(strict: Boolean = false): Int**

    Returns a random int greater than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 int.


 - **fun aNegativeInt(strict: Boolean = false): Int**

    Returns a random int less than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 int.


 - **fun aTinyInt(): Int**

    Returns a random int between 0 and 32. 


 - **fun aSmallInt(): Int**

    Returns a random int between 0 and 256. 


 - **fun aBigInt(): Int**

    Returns a random int greater than 65’536. 


 - **fun aHugeInt(): Int**

    Returns a random int greater than 16’777’216. 


 - **fun aGaussianInt(mean: Int = 0, standardDeviation: Int = 100): Int**

    Returns an int picked from a gaussian distribution (aka bell curve)

    - _mean_ : the mean value of the distribution
    - _script_ : the standard deviation value of the distribution


---

### Generating Floats

With Elmyr, you can forge floating point values (eg: `3.14`, `1.618`, `2.718`, …).

 - **fun aFloat(constraint: FloatConstraint = FloatConstraint.ANY): Float**

    Returns a random float, based on a constraint. 

    - _constraint_ : the constraint to build the float. For more information, read the [Constraints](constraints.html) page.


 - **fun aFloat(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Float**

    Returns a random float, within the given range. 
    
    - _min_ : the minimum value (inclusive).
    - _max_ : the maximum value (exclusive).


 - **fun aPositiveFloat(strict: Boolean = false): Float**

    Returns a random float greater than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 float.


 - **fun aNegativeFloat(strict: Boolean = false): Float**

    Returns a random float less than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 float.


 - **fun aGaussianFloat(mean: Float = 0, standardDeviation: Float = 100): Float**

    Returns an float picked from a gaussian distribution (aka bell curve)

    - _mean_ : the mean value of the distribution
    - _script_ : the standard deviation value of the distribution

---

### Generating Chars

With Elmyr, you can forge character values (eg: `‘m’`, `‘π’`, `‘✓’`, …).

 - **fun aChar(constraint: CharConstraint = CharConstraint.ANY, case: Case = Case.ANY): Char**

    Returns a random char, based on constraints.

    - _constraint_ : the constraint to build the char. For more information, read the [Constraints](constraints.html) page.
    - _case_ : the case to use (depending on the constraint, it might be ignored)


 - **fun aChar(min: Char = 0x20, max: Char = 0xD800): Char**

    Returns a random char, in the given range.

    - _min_ : the minimum value (inclusive).
    - _max_ : the maximum value (exclusive).


 - **fun anAsciiChar(): Char**

    Returns a random char, from the standard ASCII range.


 - **fun anExtendedAsciiChar(): Char**

    Returns a random char, from the standard ASCII range.
    

 - **fun anAlphabeticalChar(case: Case = Case.ANY): Char**

    Returns a random char, within the roman alphabet (‘a’ to ‘z’).

    - _case_ : the case to use 


 - **fun aNonAlphabeticalChar(): Char**

    Returns a random char, anything but alphabetical.


 - **fun aVowelChar(case: Case = Case.ANY): Char**

    Returns a random char, within the roman vowels.

    - _case_ : the case to use 


 - **fun aConsonantChar(case: Case = Case.ANY): Char**

    Returns a random char, within the roman consonants.

    - _case_ : the case to use 


 - **fun anAlphaNumericalChar(case: Case = Case.ANY): Char**

    Returns a random char, within the roman alphabet (‘a’ to ‘z’) or the arabic numerals (‘0’ to ‘9’).

    - _case_ : the case to use 


 - **fun aNonAlphabeticalChar(): Char**

    Returns a random char, anything but alphabetical or numerical.


 - **fun anHexadecimalChar(case: Case = Case.LOWER): Char**

    Returns a random char, within the hexadecimal characters (‘a’ to ‘f’ and ‘0’ to ‘9’).

    - _case_ : the case to use 


 - **fun aNonHexadecimalChar(): Char**

    Returns a random char, anything but hexadecimal.


 - **fun aNumericalChar(): Char**

    Returns a random char, within the the arabic numerals (‘0’ to ‘9’).


 - **fun aNonNumericalChar(): Char**

    Returns a random char, anything but numerical.


 - **fun aWhitespaceChar(): Char**

    Returns a random char representing a whitespace : a space, a tab or a new line.


 - **fun aNonNumericalChar(): Char**

    Returns a random char, anything but a whitespace.


---

### Generating Strings

With Elmyr, you can forge text values (eg: `“foo”`, `“spam”`, `“kamoulox”`, …).

 - **fun aString(constraint: StringConstraint = StringConstraint.ANY, case: Case = Case.ANY, size: Int = -1): String**

    Returns a random string, based on constraints.

    - _constraint_ : the constraint to build the char. For more information, read the [Constraints](constraints.html) page.
    - _case_ : the case to use (depending on the constraint, it might be ignored).
    - _size_ : the desired size of the string, or -1 for a random sized string.


 - **fun aWord(case: Case = Case.ANY, size: Int = -1): String**

    Returns a random string, mimicking a word (usually just some _gobbledygook_).

    - _case_ : the case to use.
    - _size_ : the desired size of the string, or -1 for a random sized string.


 - **fun aSentence(case: Case = Case.ANY, size: Int = -1): String**

    Returns a random string, mimicking a sentence structure (usually just some kind of _Lorem Ipsum_).

    - _case_ : the case to use.
    - _size_ : the desired size of the string, or -1 for a random sized string.


 - **fun anHexadecimalString(case: Case = Case.LOWER, size: Int = -1): String**

    Returns a random string, using only hexadecimal characters.

    - _case_ : the case to use (UPPER or LOWER only).
    - _size_ : the desired size of the string, or -1 for a random sized string.


 - **fun aStringMatching(regex: String): String**
 - **fun aStringMatching(regex: Regex): String**

    Returns a random string, matching the given regular-expression pattern. Note that parsing the regex can take some time depending on the regex complexity. Also not all regex feature are supported.

    - _regex_ : the regular expression to match.


 - **fun aUrl(): String**

    Returns a string following the URL format.


 - **fun anEmail(): String**

    Returns a string following the e-mail format.


---

### Generating Enums

With Elmyr, you can forge enum values. 

 - **fun <E> aValueFrom(enumClass: Class<E>): E**

    Returns a random, valid enum value.

    - _enumClass_ : the class from which to pick a value.

---

### Generating data from a Collection

With Elmyr, you can pick random data within a vararg / arrays / list / set / map. 

 - **fun <T> anElementFrom(vararg elements: T): T**
 - **fun anElementFrom(array: BooleanArray): Boolean**
 - **fun anElementFrom(array: CharArray): Char**
 - **fun anElementFrom(array: IntArray): Int**
 - **fun anElementFrom(array: FloatArray): Float**
 - **fun <T> anElementFrom(list: List<T>): T**
 - **fun <T> anElementFrom(set: Set<T>): T**
 - **fun <K,V> anElementFrom(map: Map<K,V>): Map.Entry<K,V>**

    Returns an element picked from the input collection.





