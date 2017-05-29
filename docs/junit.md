## Using Elmyr with Junit

The Elmyr library provides a couple of [Rules](https://github.com/junit-team/junit4/wiki/rules) to use in your JUnit tests : 
 
### JUnitForger

The `JUnitForger` class extends the [Forger](forger.html) class, meaning you can use it directly to forge data.
 
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

It will be automatically reset with a new seed before each test, so you don't have to do it yourself. 

If one of your test fails, `JunitForger` will add a log in the error output stream : 

> ‘fooTest’ failed with fake seed = 0x666dead999face

You can then force your test to reuse the same seed by creating an `@Before` annotated method : 

```java
public class FooTest {
    
    @Rule public JUnitForger forger = new JUnitForger();
    
    @Before
    public void setUp(){
        forger.reset(0x666dead999faceL);
    }
    
    @Test
    public void shouldDoSomething(){
        int base = forger.aPositiveInt();
        int result = square(base);
        assertThat(result).isEqualTo(base * base);
    }
}
```


### Repeater

When you start using Elmyr, and your test input become really random data, you might want to repeat your tests severall 
time, to cover more ground on each pass. 
 
In Kotlin, this can easily be done using the `repeat()` method : 

```kotlin
fun shouldDoSomething() {
    repeat(16, {
        val base = forger.aSmallInt()
        val res = square(base)
        assertThat(result).isEqualTo(base * base)
    })
}
```

In Java/JUnit, this is not as simple. A method would be to use Elmyr to generate a parameter set for a 
[Parameterized Test](https://github.com/junit-team/junit4/wiki/Parameterized-tests) : 
 
```java
@RunWith(Parameterized.class)
public class BarTest {
    
    @Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();

        for (int i = 0; i < 51; i++) {
            params.add(new Object[]{forger.aWord(), forger.anInt()});
        }
    }
    
    // …
}
```

Another option would be to use the `Repeater` class. This class takes an optional int as parameter (the default value is 0), which is the number of 
times each test will be ran. Each test run is going to reset the `JUnitForger`'s seed (if any), meaning each run is going 
to test different values. 
 
```java
public class FooTest {
    
    @Rule public JUnitForger forger = new JUnitForger();
    @Rule public Repeater repeater = new Repeater(13);
    
    // …
}
```

By default, the `Repeater` rule is global to your test class, meaning all tests will be repeated the same number of times.
If needed, you can specify a custom repeat count for a method by adding a postfix `"_x42"` 
(42 here is an unoriginal example, replace it with any int of your choice).

```java
public class FooTest {
    
    @Rule public JUnitForger forger = new JUnitForger();
    @Rule public Repeater repeater = new Repeater();
    
    // This test will be ran only once
    @Test
    public void shouldDoSomething(){
        // …
    }
    
    // This test will be ran 666 times
    @Test
    public void shouldDoSomething_x666(){
        // …
    }
}
```