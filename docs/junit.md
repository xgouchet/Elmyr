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

Another option would be to use the `Repeater` class, associated with the `@Repeat` annotation. The `@Repeat` annotation 
has one mandatory parameter, which is the number of times a test should be ran.
 
```java
public class FooTest {
    
    @Rule public JUnitForger forger = new JUnitForger();
    @Rule public Repeater repeater = new Repeater();
    
    // This test will be ran 42 times
    @Test
    @Repeat(42)
    public void shouldDoSomething(){
        // …
    }
}
```

Each test run is going to reset the `JUnitForger`'s seed (if any), meaning each run is going to use different forged values. 

In addition, the `@Repeat` annotation has a few optional parameters : 

 - **failureThreshol** : this defines the maximum number of test failure below which the test is still considered successful.
 - **ignoreThreshol** : this defines the maximum number of test ignored below which the test is still considered successful.
 
 **Warning**: the rules in a test are ordered by the field's name, alphabetically. If you want to specifically order the 
 `JUnitForger` and `Repeater` rules, then use a [RuleChain](https://github.com/junit-team/junit4/wiki/rules#rulechain). 