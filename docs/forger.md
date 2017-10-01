## Forger

The `Forger` class is the main class of the Elmyr library. 

Note that this documentation use the Kotlin syntax, but it's transparent for Java users. Note that parameters with default values are optionnal. 

### Resetting the seed

Like many random generators, the Forger class is based on a seed, which is a Long number. You can re-set the seed using the `reset` method.

 - **fun reset(seed : Long)**

    Resets this forger with the given seed. Knowing the seed allow the forger to reproduce previous data.

    - _seed_ the seed to use (try and remember to be able to reproduce a forgery).


---

### Forging Booleans

With Elmyr, you can forge Boolean values (ie : `true` or `false`). 

 - **fun aBool(probability : Float = 0.5f) : Bool**

    Returns a random boolean, with a biased probability of returning `true`.

    - _probability_ : the probability for the result to be `true`. _0.0f_ means always `false`, _1.0f_ means always `true`.


---

### Forging Ints / Longs

With Elmyr, you can forge integer values (eg: `42`, `24601`, `4815162342`, …).

 - **fun anInt(constraint: IntConstraint): Int**
 - **fun aLong(constraint: LongConstraint): Long**

    Returns a random int/long, based on a constraint. 

    - _constraint_ : the constraint to build the int. For more information, read the [Constraints](constraints.html) page


 - **fun anInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int**
 - **fun aLong(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Long**

    Returns a random int/long, within the given range. 
    
    - _min_ : the minimum value (inclusive).
    - _max_ : the maximum value (exclusive).


 - **fun aPositiveInt(strict: Boolean = false): Int**
 - **fun aPositiveLong(strict: Boolean = false): Long**

    Returns a random int/long greater than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 int.


 - **fun aNegativeInt(strict: Boolean = false): Int**
 - **fun aNegativeLong(strict: Boolean = false): Long**

    Returns a random int/long less than 0. 
    
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
 - **fun aGaussianLong(mean: Long = 0, standardDeviation: Long = 100): Long**

    Returns an int picked from a gaussian distribution (aka bell curve)

    - _mean_ : the mean value of the distribution
    - _script_ : the standard deviation value of the distribution


 - **fun aTimestamp(range: Long = ONE_YEAR): Long **
 
    Returns a long that can be used as a timestamp, picked in a range around the current time.
    
    - _range_ : the range around the current time, in milliseconds


---

### Forging Floats / Doubles

With Elmyr, you can forge floating point values (eg: `3.14`, `1.618`, `2.718`, …).

 - **fun aFloat(constraint: FloatConstraint): Float**
 - **fun aDouble(constraint: DoubleConstraint): Double**

    Returns a random float/double, based on a constraint. 

    - _constraint_ : the constraint to build the float. For more information, read the [Constraints](constraints.html) page.


 - **fun aFloat(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Float**
 - **fun aDouble(min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE): Double**

    Returns a random float/double, within the given range. 
    
    - _min_ : the minimum value (inclusive).
    - _max_ : the maximum value (exclusive).


 - **fun aPositiveFloat(strict: Boolean = false): Float**
 - **fun aPositiveDouble(strict: Boolean = false): Double**

    Returns a random float/double greater than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 float.


 - **fun aNegativeFloat(strict: Boolean = false): Float**
 - **fun aNegativeDouble(strict: Boolean = false): Double**

    Returns a random float/double less than 0. 
    
    - _script_ : if `true`, then then it will return a non 0 float.


 - **fun aGaussianFloat(mean: Float = 0, standardDeviation: Float = 100): Float**
  - **fun aGaussianDouble(mean: Double = 0, standardDeviation: Double = 100): Double**

    Returns a float/double picked from a gaussian distribution (aka bell curve)

    - _mean_ : the mean value of the distribution
    - _script_ : the standard deviation value of the distribution
    
    
---

### Forging Chars

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

### Forging Strings

With Elmyr, you can forge text values (eg: `“foo”`, `“spam”`, `“kamoulox”`, …).

 - **fun aString(constraint: StringConstraint = StringConstraint.ANY, case: Case = Case.ANY, size: Int = -1): String**

    Returns a random string, based on constraints.

    - _constraint_ : the constraint to build the String. For more information, read the [Constraints](constraints.html) page.
    - _case_ : the case to use (depending on the constraint, it might be ignored).
    - _size_ : the desired size of the string, or -1 for a random sized string.

 - **fun aString(constraint: CharConstraint, case: Case = Case.ANY, size: Int = -1): String**

    Returns a random string, based on a character constraint. 

    - _constraint_ : the constraint to build the chars in the String. For more information, read the [Constraints](constraints.html) page.
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

### Forging Enums

With Elmyr, you can forge enum values. 

 - **fun <E> aValueFrom(enumClass: Class<E>): E**

    Returns a random, valid enum value.

    - _enumClass_ : the class from which to pick a value.

---

### Forging data from a Collection

With Elmyr, you can pick random data within a vararg / arrays / list / set / map. 

 - **fun <T> anElementFrom(vararg elements: T): T**
 - **fun anElementFrom(array: BooleanArray): Boolean**
 - **fun anElementFrom(array: CharArray): Char**
 - **fun anElementFrom(array: IntArray): Int**
 - **fun anElementFrom(array: FloatArray): Float**
 - **fun anElementFrom(array: DoubleArray): Double**
 - **fun <T> anElementFrom(list: List<T>): T**
 - **fun <T> anElementFrom(set: Set<T>): T**
 - **fun <K,V> anElementFrom(map: Map<K,V>): Map.Entry<K,V>**

    Returns an element picked from the input collection.

---

### Forging arrays

With Elmyr, you can forge arrays of primitives / Strings based on constraints.

   - **fun anIntArray(constraint: IntConstraint, size: Int = -1): IntArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the data in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun anIntArray(min: Int, max: Int, size: Int = -1): IntArray**
   
      Returns a random array.
      
      - _min_ : the minimum value (inclusive) for the numbers in the array.
      - _max_ : the minimum value (exclusive) for the numbers in the array .
      - _size_ : the size of the array, or -1 for a random size.
      
   
   - **fun anIntArrayWithDistribution(mean: Int = 0, standardDeviation: Int = 100, size: Int = -1): IntArray**
   
      Returns a random array.
      
      - _mean_ : the mean value of the distribution (default : 0).
      - _standardDeviation_ : the standard deviation value of the distribution (default : 100). 
      - _size_ : the size of the array, or -1 for a random size.
            
      
   - **fun aLongArray(constraint: LongConstraint, size: Int = -1): LongArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the data in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aLongArray(min: Long, max: Long, size: Int = -1): LongArray**
   
      Returns a random array.
      
      - _min_ : the minimum value (inclusive) for the numbers in the array.
      - _max_ : the minimum value (exclusive) for the numbers in the array .
      - _size_ : the size of the array, or -1 for a random size.
      
   
   - **fun aLongArrayWithDistribution(mean: Long = 0, standardDeviation: Long = 100, size: Int = -1): LongArray**
   
      Returns a random array.
      
      - _mean_ : the mean value of the distribution (default : 0).
      - _standardDeviation_ : the standard deviation value of the distribution (default : 100). 
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aFloatArray(constraint: FloatConstraint, size: Int = -1): FloatArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the data in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aFloatArray(min: Float, max: Float, size: Int = -1): FloatArray**
   
      Returns a random array.
      
      - _min_ : the minimum value (inclusive) for the numbers in the array.
      - _max_ : the minimum value (exclusive) for the numbers in the array .
      - _size_ : the size of the array, or -1 for a random size.
      
   
   - **fun aFloatArrayWithDistribution(mean: Float = 0, standardDeviation: Float = 100, size: Int = -1): FloatArray**
   
      Returns a random array.
      
      - _mean_ : the mean value of the distribution (default : 0).
      - _standardDeviation_ : the standard deviation value of the distribution (default : 100). 
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aDoubleArray(constraint: DoubleConstraint, size: Int = -1): DoubleArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the data in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aDoubleArray(min: Double, max: Double, size: Int = -1): DoubleArray**
   
      Returns a random array.
      
      - _min_ : the minimum value (inclusive) for the numbers in the array.
      - _max_ : the minimum value (exclusive) for the numbers in the array .
      - _size_ : the size of the array, or -1 for a random size.
      
   
   - **fun aDoubleArrayWithDistribution(mean: Double = 0, standardDeviation: Double = 100, size: Int = -1): DoubleArray**
   
      Returns a random array.
      
      - _mean_ : the mean value of the distribution (default : 0).
      - _standardDeviation_ : the standard deviation value of the distribution (default : 100). 
      - _size_ : the size of the array, or -1 for a random size.
   
   
   - **fun aCharArray(constraint: CharConstraint, size: Int = -1): CharArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the data in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aCharArray(min: Char, max: Char, size: Int = -1): CharArray**
   
      Returns a random array.
      
      - _min_ : the minimum value (inclusive) for the chars in the array.
      - _max_ : the minimum value (exclusive) for the chars in the array .
      - _size_ : the size of the array, or -1 for a random size.
      
      
   - **fun aStringArray(constraint: CharConstraint, size: Int = -1): StringArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the chars of the Strings in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
      
   - **fun aStringArray(constraint: StringConstraint, size: Int = -1): StringArray**

      Returns a random array.

      - _constraint_ :  the constraint to build the Strings in the array. For more information, read the [Constraints](constraints.html) page.
      - _size_ : the size of the array, or -1 for a random size.
   

   - **fun aStringArray(regex: String, size: Int = -1): Array<String>**
   - **fun aStringArray(regex: Regex, size: Int = -1): Array<String>**
    
        Returns a random array.
        
        - _regex_ : the regular expression to match.
        - _size_ : the size of the array, or -1 for a random size.
        
   - **fun <T> aSubListOf(list: List<T>, outputSize: Int): List<T>**
   
        Returns a list with elements picked (in order) from the input list. If the input list doesn't have duplicates, then the output is guaranteed to be without duplicates.
        
        - _list_ : the input list to pick elements from.
        - _size_ : the size of the output list. If the input list is smaller, then the output will have the size of the input list.
        
---

### Forging a nullable value

All the values forged by Elmyr are guaranteed to be non null. If you want to have the odd `null` value, you can call the `nullable` method.
 
 - **fun <T> aNullableFrom(value: T, nullProbability: Float = 0.5f): T?**
 
    Returns either the input value, or null. 
    
    - _value_ the value to use
    - _nullProbability_ the probability used to return `null` instead of `value`

    You can use another method from Forger, eg : 
    
    ```kotlin
    val s = forger.aNullableFrom(forger.aString())
    ```



