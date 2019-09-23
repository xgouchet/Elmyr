package fr.xgouchet.elmyr.junit4

/**
 * Mark a field as a forgery.
 *
 * - Allows shorthand forgery creation.
 * - Minimizes repetitive forgery creation code.
 * - Makes the test class more readable.
 *
 * To be used properly, you'll also need to use an instance of [JUnitForge] annotated with [Rule].
 * ```kotlin
 * class KotlinAnnotationTest {
 *     @Rule
 *     @JvmField
 *     val forge = JUnitForge().withFactory(FooFactory())
 *
 *     @Forgery
 *     internal lateinit var fakeFoo: Foo
 *
 *     // ...
 * }
 * ```
 */
@Target(
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Forgery